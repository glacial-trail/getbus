package info.getbus.servebus.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @RequestMapping("/get/")
    @ResponseBody
    public String get() {
        return "bus";
    }

}
