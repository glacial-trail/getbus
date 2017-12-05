package info.getbus.servebus.web.controllers;

import info.getbus.servebus.repository.BRepo;
import info.getbus.servebus.service.security.RegistrationService;
import info.getbus.servebus.model.security.RegisterUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import info.getbus.servebus.model.security.RegisterUserDTO.ValidateAnyway;
import info.getbus.servebus.model.security.RegisterUserDTO.Sequence;


import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;

@Controller
public class MainController {
    @Autowired
    private BRepo bRepo;
    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/logon")
    public String logon() {
        return "logon";
    }

    @GetMapping("/")
    public String start(Principal principal, HttpServletRequest request) {
        if (principal == null) {
            return "forward:/logon";
        } else if (request.isUserInRole("ROLE_USER_BUS")) {
            return "redirect:/t/";
        } else {
            return "forward:/index";
        }
    }

    @GetMapping("/index")
    public String start(@AuthenticationPrincipal UserDetails user, ModelMap model,  HttpServletRequest request) {
        model.addAttribute("name", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("bl", bRepo.getAll());
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
    public String registerPartner(@ModelAttribute("user") RegisterUserDTO user) {
        return "register-partner";
    }

    @PostMapping("/register-partner")
    public String registerTransporter(
            @ModelAttribute("user")
            @Validated({ValidateAnyway.class, Sequence.class})
        RegisterUserDTO user,
        BindingResult errors) {
            if (errors.hasErrors()) {
                return "register-partner";
            }
        registrationService.registerTransporter(user);
        return "redirect:/";
    }
}
