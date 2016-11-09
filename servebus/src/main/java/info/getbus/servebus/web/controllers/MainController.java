package info.getbus.servebus.web.controllers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;

@Controller
public class MainController {

    @GetMapping("/logon")
    public String logon() {
        return "logon";
    }

    @GetMapping("/")
    public String start(Principal principal, HttpServletRequest request) {
        if (principal == null) {
            return "forward:/logon";
        } else if (request.isUserInRole("ROLE_USER_BUS")) {
            return "redirect:/tr/";
        } else {
            return "forward:/index";
        }
    }

    @GetMapping("/index")
    public String start(@AuthenticationPrincipal UserDetails user, ModelMap model,  HttpServletRequest request) {
        model.addAttribute("name", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        return "index";
    }


    private boolean hasRole(Collection<? extends GrantedAuthority> a, String role) {
        for (GrantedAuthority grantedAuthority : a) {
            if (grantedAuthority.toString().equals(role)) {
                return true;
            }
        }
        return false;
    }


    @GetMapping("/register-partner")
    public String registerPartner() {
        return "register-partner";
    }
}
