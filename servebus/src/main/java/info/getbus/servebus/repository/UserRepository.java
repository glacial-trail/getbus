package info.getbus.servebus.repository;

import info.getbus.servebus.model.security.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserRepository extends Repository<String,User>, UserDetailsService {
}
