package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;
import info.getbus.servebus.model.security.UserBuilder;
import info.getbus.servebus.persistence.datamappers.user.ProfileMapper;
import info.getbus.servebus.repository.UserRepository;
import info.getbus.servebus.model.security.RegisterUserDTO;
import info.getbus.servebus.service.transporter.TransporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    Logger log = Logger.getLogger(RegistrationServiceImpl.class.getCanonicalName());

    private UserRepository userRepo;
    private PasswordEncoder encoder;
    private TransporterService transporterService;
    private ProfileMapper userProfileMapper;

    public RegistrationServiceImpl(
            @Autowired UserRepository userRepo,
            @Autowired PasswordEncoder encoder,
            @Autowired TransporterService transporterService,
            @Autowired ProfileMapper userProfileMapper
    ) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.transporterService = transporterService;
        this.userProfileMapper = userProfileMapper;
    }

    public void registerUser(RegisterUserDTO user) {
        register(user, "ROLE_USER_PASSENGER");
    }

    @Transactional
    @Override
    public void registerTransporter(RegisterUserDTO transporterUser) {
        log.info("registering new transporter: " + transporterUser);
        Long transporterAreaId =  transporterService.createInitialTransporterInfrastructure();
        User registeredUser = register(transporterUser, "ROLE_USER_BUS");
        userProfileMapper.insert(registeredUser.getUsername(), "Europe/Kiev");
        transporterService.linkUserToArea(transporterAreaId, registeredUser, "ROLE_USER_BUS");
    }

    private UserBuilder prepareBuilder(RegisterUserDTO dto) {
        return new UserBuilder().fromDTO(dto).setPassword(encoder.encode(dto.getPassword())).setEnabled(true);
    }

    private User register(RegisterUserDTO udto, String role) {
        User user = prepareBuilder(udto).addAuthority(new SimpleGrantedAuthority(role)).build();
        userRepo.save(user);
        return user;
    }
}
