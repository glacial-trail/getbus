package info.getbus.servebus.dao.security;


import info.getbus.servebus.dao.Mapper;
import info.getbus.servebus.model.security.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserMapper extends Mapper<String,User> {
    void insertRoles(@Param("username")String username, @Param("roles")Collection<? extends GrantedAuthority> roles);
}
