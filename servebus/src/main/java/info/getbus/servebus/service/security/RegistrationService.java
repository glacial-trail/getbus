package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.RegisterUserDTO;

public interface RegistrationService {
    void registerUser(RegisterUserDTO user);
    void registerTransporter(RegisterUserDTO user);
}
