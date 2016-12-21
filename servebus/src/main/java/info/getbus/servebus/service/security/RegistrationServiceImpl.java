package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;
import info.getbus.servebus.model.security.UserBuilder;
import info.getbus.servebus.repository.UserRepository;
import info.getbus.servebus.model.security.RegisterUserDTO;
import info.getbus.servebus.service.validation.UniqueValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.logging.Logger;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    Logger log = Logger.getLogger(RegistrationServiceImpl.class.getCanonicalName());

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder encoder;


    public void registerUser(RegisterUserDTO udto) throws UniqueValidationException {
        User user = prepareBuilder(udto).addAuthority(new SimpleGrantedAuthority("ROLE_USER_PASSENGER")).build();
        validateUniqueness(user);
        register(user);
    }

    @Override
    public void registerTransporter(RegisterUserDTO udto) throws UniqueValidationException {
        log.info("registering user: " + udto);
        User user = prepareBuilder(udto).addAuthority(new SimpleGrantedAuthority("ROLE_USER_BUS")).build();
        validateUniqueness(user);
        register(user);
    }

    private void validateUniqueness(User user) throws UniqueValidationException {
        Collection<User> users = userRepo.getByUsernameOrPhone(user);
        if (!users.isEmpty()) {
            UniqueValidationException e = new UniqueValidationException();
            for (User u : users) {
                if (user.getEmail().equals(u.getEmail())) {
                    e.setEmail(true);
                }

                if (user.getPhone().equals(u.getPhone())) {
                    e.setPhone(true);
                }
            }
            throw e;
        }
    }


    private UserBuilder prepareBuilder(RegisterUserDTO dto) {
        return new UserBuilder().fromDTO(dto).setPassword(encoder.encode(dto.getPassword())).setEnabled(true);
    }

    private void register(User user) {
        userRepo.save(user);
    }
}
