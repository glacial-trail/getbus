package info.getbus.servebus.route.persistence.managers;

import info.getbus.servebus.model.security.User;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.WayPoint;
import info.getbus.servebus.route.persistence.LockedRouteException;
import info.getbus.servebus.route.persistence.mappers.RouteMapper;
import info.getbus.servebus.route.persistence.mappers.WayPointMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class RoutePersistenceManagerImpl implements RoutePersistenceManager {
    private final RouteMapper routeMapper;
    private final WayPointMapper wayPointMapper;

    public void savePoints(Route route) {
        log.debug("Saving points for route {} {}, points amount {}", route.getId(), route.getDirection(), route.getWayPoints().size());
        if (route.isForward()) {
            wayPointMapper.negateSequence(route.getId());
        }
        for (WayPoint wp : route.getRoutePointsInNaturalOrder()) {
            if (route.isForward()) {
                wayPointMapper.upsert(wp);
            }
            wayPointMapper.upsertLength(wp, route.getDirection());
            wayPointMapper.upsertTimetable(wp, route.getDirection());
        }
        if (route.isForward()) {
            wayPointMapper.deleteOutOfRange(route.getId(), route.getWayPoints().size()); // or .getLastStop().getSequence())
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
