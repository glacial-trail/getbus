package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;
import info.getbus.servebus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    private UserRepository userRepo;

    public void registerUser(User user) {
/*
        ...
        userRepo.save(user);

        ...
*/
    }

    @Override
    public void registerTransporter(User user) {
/*
        ...

        registerUser(user);

        ...

*/
    }
}
