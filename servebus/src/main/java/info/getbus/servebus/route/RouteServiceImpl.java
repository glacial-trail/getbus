package info.getbus.servebus.route;

import info.getbus.servebus.route.model.CompactRoute;
import info.getbus.servebus.route.model.PeriodicityPair;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RoutePartId;
import info.getbus.servebus.route.model.RoutePeriodicity;
import info.getbus.servebus.model.security.User;
import info.getbus.servebus.route.persistence.mappers.RouteMapper;
import info.getbus.servebus.route.persistence.mappers.RouteStopMapper;
import info.getbus.servebus.route.persistence.managers.RoutePeriodicityPersistenceManager;
import info.getbus.servebus.route.persistence.managers.RoutePersistenceManager;
import info.getbus.servebus.service.security.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

//TODO get rid of mappers in service
@Service
@Transactional
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private RouteStopMapper routeStopMapper;
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

    @Override //TODO decorate by calculator reversed distances or route calculate itself?
    public Route get(RoutePartId id) {
        return routeMapper.selectById(id);
    }

    @Override
    public Route acquireForEdit(RoutePartId partId) {
        tryLock(partId.getId());
        Route route = get(partId);
        return adjustTimezone(route);//TODO decorator fom mapper interface?
    }//or adjust tz only for display on web

    private Route adjustTimezone(Route route) {
        ZoneId currentUserTimezone = securityHelper.getCurrentUser().getProfile().getTimeZone();
        ZonedDateTime adjustedTimezone = route.getStartSales().withZoneSameInstant(currentUserTimezone);
        route.setStartSales(adjustedTimezone);
        return route;
    }

    @Override
    public void acquireLock(long routeId) {
        tryLock(routeId);
    }

    @Override
    public void cancelEdit(long routeId) {
        releaseConsistent(routeId);
    }

    @Override
    public void releaseConsistent(long routeId) {
        checkLock(routeId);
        unlockConsistent(routeId);
    }

    private void unlockConsistent(long routeId) {
        if (isConsistent(routeId)) {
            routeMapper.unlockRoute(routeId);
        }
    }

    @Override
    public boolean saveAndCheckConsistency(Route route, boolean unlockIfConsistent) {
        if (null == route.getId()) {
            if (route.isForward()) {
                createLocked(route);
                return false;
            } else {
                throw new MalformedArgumentException("New route can't be reverse");// really?
            }
        } else {
            tryLock(route);
            update(route);
            if (isConsistent(route)) {
                if (unlockIfConsistent) {
                    unlock(route);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private void checkLock(Route route) {
        checkLock(route.getId());
    }
    private void checkLock(long routeId) {
        persistenceManager.checkLock(routeId, resolveCurrentUserName());
    }

    private void update(Route route) {
        if (route.isForward()) {
            routeMapper.update(route);
        }
        persistenceManager.saveStops(route);
    }

    private void unlock(Route route) {
        routeMapper.unlockRoute(route.getId());
    }

    private boolean isConsistent(Route route) {
        return isConsistent(route.getId());
    }
    private boolean isConsistent(Long routeId) {
        return !routeStopMapper.existInconsistentRoutePoints(routeId);
    }

    private void tryLock(Route route) {
        tryLock(route.getId());
    }
    private void tryLock(Long id) {
        persistenceManager.tryLockFor(id, resolveCurrentUserName());
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
    //TODO maybe lock it on display phase? like route
    @Override
    public void savePeriodicity(PeriodicityPair pair) {
        checkLock(pair.getRouteId());
        savePeriodicity(pair.getForward());
        savePeriodicity(pair.getReverse());
        unlockConsistent(pair.getRouteId());
    }

    private void savePeriodicity(RoutePeriodicity periodicity) {
        routePeriodicityPersistenceManager.save(periodicity.getRoutePartId(), periodicity.getPeriodicity());
    }
}
