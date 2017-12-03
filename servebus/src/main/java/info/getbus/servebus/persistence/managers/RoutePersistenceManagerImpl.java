package info.getbus.servebus.persistence.managers;

import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.RoutePoint;
import info.getbus.servebus.model.security.User;
import info.getbus.servebus.persistence.LockedEntityException;
import info.getbus.servebus.persistence.datamappers.route.RouteMapper;
import info.getbus.servebus.persistence.datamappers.route.RoutePointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

@Service
public class RoutePersistenceManagerImpl implements RoutePersistenceManager {
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private RoutePointMapper routePointMapper;

    public static class PointId2Sequence {
        private Long id;
        private int sequence;

        PointId2Sequence(Long id, int seq) {
            this.id = id;
            this.sequence = seq;
        }

        Long getId() {
            return id;
        }

        int getSequence() {
            return sequence;
        }
    }

    public void savePoints(Route route) {
        Deque<PointId2Sequence> points = new LinkedList<>();
        int c = 0;

        Iterator<RoutePoint> pointItr = route.isForward() ? route.getRoutePoints().iterator() : route.getRoutePoints().descendingIterator();
        while (pointItr.hasNext()) {
            RoutePoint routePoint = pointItr.next();
            c++;
            if (null == routePoint.getId()) {
                routePointMapper.insert(route.getId(), routePoint, -1 * c);
            } else {
                routePointMapper.update(routePoint);
            }
//          TODO upsert
            routePointMapper.insertDataIfNonExist(routePoint, route.getDirection());
            routePointMapper.updateData(routePoint, route.getDirection());

            points.add(new PointId2Sequence(routePoint.getId(), c));
        }

        for (Iterator<PointId2Sequence> pointSequenceItr = points.descendingIterator(); pointSequenceItr.hasNext();) {
            PointId2Sequence point = pointSequenceItr.next();
            routePointMapper.updateSequence(point.getId(), point.getSequence());
        }
    }

    public void createRoute(Route route, User forUser, boolean lock) {
        if (lock) {
            routeMapper.insertLocked(forUser.getTransporterAreaId(), route, forUser.getUsername());
            savePoints(route);
        } else {
            //TODO implement
            throw new RuntimeException("Not implemented");
        }
    }

    public Route prepareReversed(Route route) {
        route.setDirection(route.isForward() ? Direction.R : Direction.F);
        Deque<RoutePoint> routePoints = routePointMapper.selectRoutePointsWithData(route.getId(), route.getDirection());
        route.setRoutePoints(routePoints);
        return route;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void  tryToLockFor(Long routeId, String me) {
        String lockOwner = routeMapper.selectLockOwnerForUpdate(routeId);
        if (null == lockOwner) {
            routeMapper.updateLockOwner(routeId, me);
        } else if (!lockOwner.equals(me)) {
            throw new LockedEntityException(routeId, me, lockOwner);
        }
    }
}
