package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class DefaultSecurityHelper implements SecurityHelper {
    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return  (User)principal;
        }
        throw new RuntimeException("Illegal type of principal for GetBus: " + principal.getClass());
    }

    @Override
    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    @Override
    public boolean isRouteOwner(String username, Long routeId) {
        throw new NotImplementedException();
    }
}
