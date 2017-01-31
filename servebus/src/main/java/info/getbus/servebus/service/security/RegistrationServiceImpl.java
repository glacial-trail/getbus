package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;
import info.getbus.servebus.model.security.UserBuilder;
import info.getbus.servebus.repository.UserRepository;
import info.getbus.servebus.model.security.RegisterUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    Logger log = Logger.getLogger(RegistrationServiceImpl.class.getCanonicalName());

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder encoder;


    public void registerUser(RegisterUserDTO udto) {
        User user = prepareBuilder(udto).addAuthority(new SimpleGrantedAuthority("ROLE_USER_PASSENGER")).build();
        register(user);
    }

    @Override
    public void registerTransporter(RegisterUserDTO udto) {
        log.info("registering user: " + udto);
        User user = prepareBuilder(udto).addAuthority(new SimpleGrantedAuthority("ROLE_USER_BUS")).build();
        register(user);
    }

    private UserBuilder prepareBuilder(RegisterUserDTO dto) {
        return new UserBuilder().fromDTO(dto).setPassword(encoder.encode(dto.getPassword())).setEnabled(true);
    }

    private void register(User user) {
        userRepo.save(user);
    }
}
