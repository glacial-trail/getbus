package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DummyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;


    public UserDetails loadUserByUsername(String logonid) throws UsernameNotFoundException {
        User user = userService.getUser(logonid);
        Set<GrantedAuthority> roles = new HashSet<>();
        if (logonid.contains("found")) {
            throw new UsernameNotFoundException("user not found");
        } else if (logonid.contains("user")) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            roles.add(new SimpleGrantedAuthority("ROLE_USER_PASSENGER"));
        } else {
            roles.add(new SimpleGrantedAuthority("ROLE_USER_BUS"));
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), roles);
    }

}
