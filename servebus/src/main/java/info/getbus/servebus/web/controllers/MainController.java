package info.getbus.servebus.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @RequestMapping("/get/")
    @ResponseBody
    public String get() {
        return "bus";
    }

    @RequestMapping(value = "/fget/", method = RequestMethod.GET)
    public String fget(ModelMap model) {
        model.addAttribute("title", "get");
        model.addAttribute("free", "bus");
        return "fget";
    }
    @RequestMapping(value = "/mfget/", method = RequestMethod.GET)
    public String mfget(@ModelAttribute("model") ModelMap model) {
        model.addAttribute("title", "mget");
        model.addAttribute("free", "mbus");
        return "fget";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String start(Model model){
        return "index";
    }
}
