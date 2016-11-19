package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;

public interface RegistrationService {
    void registerUser(User user);
    void registerTransporter(User user);
}
