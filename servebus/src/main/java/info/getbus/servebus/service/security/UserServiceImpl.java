package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    public User getUser(String login) {
        User user = new User();
        user.setLogin(login);
        user.setPassword("40bd001563085fc35165329ea1ff5c5ecbdbbeef");//123

        return user;
    }

}