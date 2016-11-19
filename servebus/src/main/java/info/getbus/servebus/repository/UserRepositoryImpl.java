package info.getbus.servebus.repository;

import info.getbus.servebus.dao.security.UserMapper;
import info.getbus.servebus.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@org.springframework.stereotype.Repository
public class UserRepositoryImpl extends AbstractRepository<String, User> implements UserRepository {
    @Autowired
    UserMapper mapper;

    @Override
    protected UserMapper mapper() {
        return mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return get(username);
    }
}
