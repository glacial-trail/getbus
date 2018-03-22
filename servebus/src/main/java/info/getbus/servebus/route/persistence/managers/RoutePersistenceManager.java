package info.getbus.servebus.route.persistence.managers;

import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.model.security.User;

public interface RoutePersistenceManager {
    void createRoute(Route route, User forUser, boolean lock);
    void savePoints(Route route);
    @Deprecated
    Route prepareReversed(Route route);
    void tryLockFor(Long routeId, String me);
    void checkLock(long routeId, String me);
}
