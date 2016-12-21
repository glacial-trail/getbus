package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.RegisterUserDTO;
import info.getbus.servebus.service.validation.UniqueValidationException;

public interface RegistrationService {
    void registerUser(RegisterUserDTO user) throws UniqueValidationException;
    void registerTransporter(RegisterUserDTO user) throws UniqueValidationException;
}
