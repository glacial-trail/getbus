package info.getbus.servebus.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/tr/")
public class TransporterMainController {

    @GetMapping("/")
    public String cab() {
        return "tr/cab";
    }
}
