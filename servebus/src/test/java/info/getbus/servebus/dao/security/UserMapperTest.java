package info.getbus.servebus.dao.security;

import info.getbus.servebus.model.security.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@ContextConfiguration(locations = {"classpath:persistence-config.xml" })
@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMapperTest {
    @Autowired
    private UserMapper mapper;

    private String username = "khun@com";
    private User user;

    private GrantedAuthority authority1 = new SimpleGrantedAuthority("BUSSY");
    private GrantedAuthority authority2 = new SimpleGrantedAuthority("USEROK");
    private String phone = "+123456784444";

    @Before
    public void setUp() throws Exception {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(authority1);
        roles.add(authority2);
        user = new User(username,"***",true, "khun", "czen", phone);
        user.setAuthorities(roles);

        mapper.insert(user);
        mapper.insertRoles(user.getUsername(), roles);
    }

    @Test
    public void testNotNull() throws Exception {
        assertNotNull(mapper);
    }

    @Test
    public void testSelectById() throws Exception {
        User user = mapper.selectById(username);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals("***", user.getPassword());
        assertTrue(user.isEnabled());
        assertEquals("khun", user.getFirstname());
        assertEquals("czen", user.getLastname());
        assertEquals(phone, user.getPhone());
        assertCollectionNotEmpty(user.getAuthorities());
        Collection<? extends GrantedAuthority> a = user.getAuthorities();
        assertCollectionSize(2, a);
        assertTrue(a.contains(authority1));
        assertTrue(a.contains(authority2));
    }

    @Test
    public void testInsert() throws Exception {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(authority1);
        roles.add(authority2);

        User user = new User("###","***",true, "khun", "czen", "+123456789012");
        user.setAuthorities(roles);
        mapper.insert(user);
        mapper.insertRoles(user.getUsername(), roles);

        User selected = mapper.selectById("###");
        assertNotNull(selected);
        assertCollectionNotEmpty(selected.getAuthorities());
    }

    //TODO create more users for test
    @Test
    public void testSelectByUsernameOrPhone() throws Exception {
        assertCollectionSize(1, mapper.selectByUsernameOrPhone(username, phone));
        assertCollectionSize(1, mapper.selectByUsernameOrPhone(username, "dhgfw"));
        assertCollectionSize(1, mapper.selectByUsernameOrPhone("cktfw", phone));
    }

    private void assertCollectionSize(int size, Collection<?> c) {
        assertEquals(size, c.size());
    }

    private void assertCollectionNotEmpty(Collection<?> c) {
        assertNotNull(c);
        assertFalse(c.isEmpty());
    }
}