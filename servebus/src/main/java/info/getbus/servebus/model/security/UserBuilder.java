package info.getbus.servebus.model.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

public class UserBuilder {
    private String username;
    private String password;
    private boolean enabled;
    private String firstname;
    private String lastname;
    private String phone;
    private Collection<GrantedAuthority> authorities = new HashSet<>();

    public UserBuilder fromDTO(RegisterUserDTO user) {
        setUsername(user.getEmail());
        setFirstname(user.getFirstname());
        setLastname(user.getLastname());
        setPhone(user.getPhone());
        return this;
    }
    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }
    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }
    public UserBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    public UserBuilder setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }
    public UserBuilder setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }
    public UserBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }
    public UserBuilder setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = new HashSet<>(authorities);
        return this;
    }
    public UserBuilder addAuthority(GrantedAuthority authority) {
        authorities.add(authority);
        return this;
    }

    public User build() {
        return new User(username,password,enabled,authorities,firstname,lastname,phone);
    }
}
