package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;

public interface UserService {
    User getUser(String login);
}
