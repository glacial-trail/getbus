package info.getbus.servebus.repository;

import info.getbus.servebus.dao.security.UserMapper;
import info.getbus.servebus.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

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

    @Override
    @Transactional
    public User save(User user) {
//        super.save(user);
        mapper().insert(user);
        mapper().insertRoles(user.getUsername(), user.getAuthorities());
        return user;
    }

    @Override
    public Collection<User> getByUsernameOrPhone(User user) {
        return mapper().selectByUsernameOrPhone(user.getEmail(), user.getPhone());
    }
}
