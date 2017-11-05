package info.getbus.servebus.service.transporter;

import info.getbus.servebus.model.route.*;
import info.getbus.servebus.persistence.managers.RoutePeriodicityPersistenceManager;
import info.getbus.servebus.persistence.managers.RoutePersistenceManager;
import info.getbus.servebus.persistence.datamappers.route.RouteMapper;
import info.getbus.servebus.persistence.datamappers.route.RoutePointMapper;
import info.getbus.servebus.model.security.User;
import info.getbus.servebus.service.MalformedArgumentException;
import info.getbus.servebus.service.security.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private RoutePointMapper routePointMapper;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private RoutePersistenceManager persistenceManager;
    @Autowired
    private RoutePeriodicityPersistenceManager routePeriodicityPersistenceManager;

    private String resolveCurrentUserName() {
        return securityHelper.getCurrentUsername();
    }
    private User resolveCurrentUser() {
        return securityHelper.getCurrentUser();
    }

    //TODO pagination
    @Override
    public List<CompactRoute> list() {
        //TODO editable if no tickets
        return routeMapper.selectCompactRoutesByUsername(resolveCurrentUserName());
    }

    @Override
    public Route get(RoutePartId id) {
        return routeMapper.selectById(id);
    }

    @Override
    public Route acquireForEdit(long routeId) {
        tryToLock(routeId);
        Route route = get(RoutePartId.forward(routeId));
        return adjustTimezone(route);
    }

    private Route adjustTimezone(Route route) {
        ZoneId currentUserTimezone = securityHelper.getCurrentUser().getProfile().getTimeZone();
        ZonedDateTime adjustedTimezone = route.getStartSales().withZoneSameInstant(currentUserTimezone);
        route.setStartSales(adjustedTimezone);
        return route;
    }

    @Override
    public void acquireLock(long routeId) {
        tryToLock(routeId);
    }

    @Override
    public void cancelEdit(long routeId) {
        releaseConsistent(routeId);
    }

    @Override
    public void releaseConsistent(long routeId) {
        tryToLock(routeId);
        unlockConsistent(routeId);
    }

    private void unlockConsistent(long routeId) {
        if (isConsistent(routeId)) {
            routeMapper.unlockRoute(routeId);
        }
    }

    @Nullable
    @Override
    public Route saveAndProceed(Route route) {
        if (null == route.getId()) {
            if (route.isForward()) {
                createLocked(route);
                return invert(route);
            } else {
                throw new MalformedArgumentException("New route can't be reverse");
            }
        } else {
            tryToLock(route);
            update(route);
            if (isConsistent(route)) {
                if (route.isForward()) {
                    return invert(route);
                } else {
                    unlock(route);
                    return null;
                }
            } else {
                return invert(route);
            }
        }
    }

    private void update(Route route) {
        if (route.isForward()) {
            routeMapper.update(route);
        }
        persistenceManager.savePoints(route);
    }

    private void unlock(Route route) {
        routeMapper.unlockRoute(route.getId());
    }

    private boolean isConsistent(Route route) {
        return isConsistent(route.getId());
    }
    private boolean isConsistent(Long routeId) {
        return !routePointMapper.existInconsistentRoutePoints(routeId);
    }

    private void tryToLock(Route route) {
        tryToLock(route.getId());
    }
    private void tryToLock(Long id) {
        persistenceManager.tryToLockFor(id, resolveCurrentUserName());
    }

    private Route invert(Route route) {
        // TODO calculate reverse distance
        return persistenceManager.prepareReversed(route);
    }

    @Nullable
    @Override
    public PeriodicityPair getPeriodicityPair(long routeId) {
        return routePeriodicityPersistenceManager.getPair(routeId);
    }

    private void createLocked(Route route) {
        persistenceManager.createRoute(route, resolveCurrentUser(), true);
    }

    //TODO create RouteServiceFacade for methods like this
    @Override
    public void savePeriodicity(PeriodicityPair pair) {
        tryToLock(pair.getRouteId());
        savePeriodicity(pair.getForward());
        savePeriodicity(pair.getReverse());
        unlockConsistent(pair.getRouteId());
    }

    private void savePeriodicity(RoutePeriodicity periodicity) {
        routePeriodicityPersistenceManager.save(periodicity.getRoutePartId(), periodicity.getPeriodicity());
    }
}
