package info.getbus.servebus.persistence.managers;

import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.WayPoint;
import info.getbus.servebus.model.security.User;
import info.getbus.servebus.persistence.LockedRouteException;
import info.getbus.servebus.persistence.datamappers.route.RouteMapper;
import info.getbus.servebus.persistence.datamappers.route.WayPointMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

@Service
@Log4j2
public class RoutePersistenceManagerImpl implements RoutePersistenceManager {
    private final RouteMapper routeMapper;
    private final WayPointMapper wayPointMapper;


    @Autowired
    public RoutePersistenceManagerImpl(RouteMapper routeMapper, WayPointMapper wayPointMapper) {
        this.routeMapper = routeMapper;
        this.wayPointMapper = wayPointMapper;
    }

    @AllArgsConstructor
    @Getter
    public static class IdSequence {
        private Long id;
        private int sequence;
    }

    private class PointSequenceStack implements Iterable<IdSequence> {
        Deque<IdSequence> stack = new LinkedList<>();
        private int c;

        public void push(WayPoint wp) {
            stack.push(new IdSequence( wp.getId(), ++c));
        }

        @Override
        public Iterator<IdSequence> iterator() {
            return stack.iterator();
        }
    }

    public void savePoints(Route route) {
        Collection<Long> ids = new LinkedList<>();
        PointSequenceStack sequenceStack = new PointSequenceStack();
        log.debug("Saving points for route {} {}, points amount {}", route.getId(), route.getDirection(), route.getWayPoints().size());
        int c = 1;
        for (WayPoint wp : route.getRoutePointsInNaturalOrder()) {
            if (null == wp.getId()) {
                wayPointMapper.insert(route.getId(), wp, -1 * c++);
                wayPointMapper.insertData(wp, route.getDirection());
            } else {
                wayPointMapper.update(wp);
                upsertData(route, c++, wp);
            }
            sequenceStack.push(wp);
            ids.add(wp.getId());
        }
        wayPointMapper.deleteNotIn(route.getId(), ids);
        sequenceStack.forEach(wp -> wayPointMapper.updateSequence(wp.getId(), wp.getSequence()));
    }

    private void upsertData(Route route, int c, WayPoint wayPoint) {
        int rowsInserted = wayPointMapper.insertDataIfNonExist(wayPoint, route.getDirection());
        if (0 == rowsInserted) {
            wayPointMapper.updateData(wayPoint, route.getDirection());
        }
    }

    @Override
    public void createRoute(Route route, User forUser, boolean lock) {
        if (lock) {
            routeMapper.insertLocked(forUser.getTransporterAreaId(), route, forUser.getUsername());
            savePoints(route);
        } else {
            //TODO implement
            throw new RuntimeException("Not implemented");
        }
    }

    @Deprecated
    @Override
    public Route prepareReversed(Route route) {
        route.setDirection(route.isForward() ? Direction.R : Direction.F);
        Deque<WayPoint> wayPoints = wayPointMapper.selectFullWayPoints(route.getId(), route.getDirection());
        route.setWayPoints(wayPoints);
        return route;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void tryLockFor(Long routeId, String me) {
        String lockOwner = routeMapper.selectLockOwnerForUpdate(routeId);
        if (null == lockOwner) {
            routeMapper.updateLockOwner(routeId, me);
        } else if (!lockOwner.equals(me)) {
            throw new LockedRouteException(routeId, me, lockOwner);
        }
    }

    @Override
    public void  checkLock(long routeId, String me) {
        String lockOwner = routeMapper.selectLockOwner(routeId);
        if (null == lockOwner || !me.equals(lockOwner)) {
            throw new LockedRouteException(routeId, me, lockOwner);
        }
    }
}
