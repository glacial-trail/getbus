package info.getbus.servebus.web.service;

/**
 * Created by art on 23.09.16.
 */

import info.getbus.servebus.web.entity.User;

public interface UserService {
    User getUser(String login);
}
