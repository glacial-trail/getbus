package info.getbus.servebus.persistence.managers;

import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.RoutePoint;
import info.getbus.servebus.model.security.User;
import info.getbus.servebus.persistence.LockedRouteException;
import info.getbus.servebus.persistence.datamappers.route.RouteMapper;
import info.getbus.servebus.persistence.datamappers.route.RoutePointMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

@Service
public class RoutePersistenceManagerImpl implements RoutePersistenceManager {
    private final RouteMapper routeMapper;
    private final RoutePointMapper routePointMapper;


    @Autowired
    public RoutePersistenceManagerImpl(RouteMapper routeMapper, RoutePointMapper routePointMapper) {
        this.routeMapper = routeMapper;
        this.routePointMapper = routePointMapper;
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

        public void push(RoutePoint wp) {
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
        int c = 1;
        for (RoutePoint wp : route.getRoutePointsInNaturalOrder()) {
            if (null == wp.getId()) {
                routePointMapper.insert(route.getId(), wp, -1 * c++);
                routePointMapper.insertData(wp, route.getDirection());
            } else {
                routePointMapper.update(wp);
                upsertData(route, c++, wp);
            }
            sequenceStack.push(wp);
            ids.add(wp.getId());
        }
        routePointMapper.deleteNotIn(route.getId(), ids);
        sequenceStack.forEach(wp -> routePointMapper.updateSequence(wp.getId(), wp.getSequence()));
    }

    private void upsertData(Route route, int c, RoutePoint routePoint) {
        int rowsInserted = routePointMapper.insertDataIfNonExist(routePoint, route.getDirection());
        if (0 == rowsInserted) {
            routePointMapper.updateData(routePoint, route.getDirection());
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
        Deque<RoutePoint> routePoints = routePointMapper.selectRoutePointsWithData(route.getId(), route.getDirection());
        route.setRoutePoints(routePoints);
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
