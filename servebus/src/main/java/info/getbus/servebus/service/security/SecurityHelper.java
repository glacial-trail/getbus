package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;

public interface SecurityHelper {
    User getCurrentUser();
    String getCurrentUsername();

    boolean isRouteOwner(String username, Long routeId);
}
