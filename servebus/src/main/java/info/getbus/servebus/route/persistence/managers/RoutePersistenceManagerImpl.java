package info.getbus.servebus.route.persistence.managers;

import info.getbus.servebus.model.security.User;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RouteStop;
import info.getbus.servebus.route.persistence.LockedRouteException;
import info.getbus.servebus.route.persistence.mappers.RouteMapper;
import info.getbus.servebus.route.persistence.mappers.RouteStopMapper;
import info.getbus.servebus.route.annotation.SelectUpdateInsert;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class RoutePersistenceManagerImpl implements RoutePersistenceManager {
    private final RouteMapper routeMapper;
    private final RouteStopMapper routeStopMapper;

    @SelectUpdateInsert
    @Autowired
    private RouteStopUpsertStrategy routeStopUpsertStrategy;

    public void saveStops(Route route) {
        log.debug("Saving stops for route {} {}, stops amount {}", route.getId(), route.getDirection(), route.getStops().size());
        if (route.isForward()) {
            routeStopMapper.negateSequence(route.getId());
        }
        for (RouteStop stop : route.getRoutePointsInNaturalOrder()) {
            if (route.isForward()) {
                routeStopUpsertStrategy.upsert(stop);
            }
            routeStopUpsertStrategy.upsertLength(stop, route.getDirection());
            routeStopUpsertStrategy.upsertTimetable(stop, route.getDirection());
        }
        if (route.isForward()) {
            routeStopMapper.deleteOutOfRange(route.getId(), route.getStops().size()); // or .getLastStop().getSequence())
        }
    }

    @Override
    public void createRoute(Route route, User forUser, boolean lock) {
        if (lock) {
            routeMapper.insertLocked(forUser.getTransporterAreaId(), route, forUser.getUsername());
            saveStops(route);
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
