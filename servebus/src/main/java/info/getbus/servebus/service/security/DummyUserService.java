package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DummyUserService implements UserService {

    @Autowired
    BCryptPasswordEncoder encoder;

    public User getUser(String login) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(encoder.encode("123"));
        return user;
    }

}