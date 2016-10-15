package info.getbus.servebus.web.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {

    @GetMapping("/logon/")
    public String logon() {
        return "logon";
    }

    @GetMapping("/")
    public String start(Principal principal){
        if (principal == null) {
            return "forward:/login";
        } else {
            return "forward:/index";
        }
    }

    @GetMapping("/index")
    public String start(@AuthenticationPrincipal UserDetails user, ModelMap model) {

        if (user != null) {
            model.addAttribute("name", user.getUsername());
            model.addAttribute("roles", user.getAuthorities());
            return "index";
        } else {
            return "redirect:/login";
        }
    }
}
