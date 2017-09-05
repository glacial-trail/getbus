package info.getbus.servebus.persistence.managers;

import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.security.User;

public interface RoutePersistenceManager {
    void createRoute(Route route, User forUser, boolean lock);
    void savePoints(Route route);
    Route prepareReversed(Route route);
    void tryToLockFor(Long routeId, String me);
}
