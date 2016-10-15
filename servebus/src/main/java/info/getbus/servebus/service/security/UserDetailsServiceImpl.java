package info.getbus.servebus.service.security;

import info.getbus.servebus.model.security.User;
import info.getbus.servebus.model.security.UserRole;


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
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;


    public UserDetails loadUserByUsername(String logonid) throws UsernameNotFoundException {
        User user = userService.getUser(logonid);
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(UserRole.ADMIN.name()));
        roles.add(new SimpleGrantedAuthority(UserRole.USER_BUS.name()));

        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), roles);

        return userDetails;
    }

}
