package info.getbus.servebus.service.transporter;

import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.persistence.RoutePersistenceManager;
import info.getbus.servebus.persistence.datamappers.route.RouteMapper;
import info.getbus.servebus.persistence.datamappers.route.RoutePointMapper;
import info.getbus.servebus.model.security.User;
import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.service.MalformedArgumentException;
import info.getbus.servebus.service.security.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.*;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private RoutePointMapper routePointMapper;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private RoutePersistenceManager persistenceManager;

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
    @Transactional
    public Route acquireForEdit(long routeId) {
        tryToLock(routeId);
        return routeMapper.selectById(routeId);
    }

    @Override
    @Transactional
    public void cancelEdit(long routeId) {
        tryToLock(routeId);
        if (isConsistent(routeId)) {
            routeMapper.unlockRoute(routeId);
        }
    }

    @Nullable
    @Override
    @Transactional
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
        // TODO update records in route table
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

    private void createLocked(Route route) {
        persistenceManager.createRoute(route, resolveCurrentUser(), true);
    }

}
