package info.getbus.servebus.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(path = {"/", "/logon/"})
    public String logon() {
        return "logon";
    }

    @GetMapping("/register-partner")
    public String registerPartner() {
        return "register-partner";
    }
}
