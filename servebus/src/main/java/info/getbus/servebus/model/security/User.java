package info.getbus.servebus.model.security;

import info.getbus.servebus.model.Entity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

public class User extends org.springframework.security.core.userdetails.User implements Entity<String> {
    private String firstname;
    private String lastname;
    private String phone;

    /* TODO make more complex
    * optional connection with transporter
    * */
    @Nullable
    private Long transporterAreaId;

    private Set<GrantedAuthority> authorities;


    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public User(String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities, String firstname, String lastname, String phone) {
        super(username, password, enabled, true, true, true, authorities);
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
    }

    public User(String username, String password, boolean enabled, String firstname, String lastname, String phone) {
        super(username, password, enabled, true, true, true, new LinkedList<GrantedAuthority>());
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return getUsername();
    }

    @Override
    public String getId() {
        return getUsername();
    }

    @Override
    public void setId(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities().isEmpty() ? authorities : super.getAuthorities();
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = sortAuthorities(authorities);
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
                new AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority,
                    "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>,Serializable {
        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            }
            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

    @Nullable
    public Long getTransporterAreaId() {
        return transporterAreaId;
    }

    public void setTransporterAreaId(@Nullable Long transporterAreaId) {
        this.transporterAreaId = transporterAreaId;
    }
}