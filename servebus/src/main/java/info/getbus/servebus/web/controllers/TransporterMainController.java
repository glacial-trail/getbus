package info.getbus.servebus.web.controllers;

import info.getbus.servebus.web.views.TransporterCabView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/tr/")
public class TransporterMainController {

    @GetMapping("/")
    public ModelAndView welcome() {
        return new TransporterCabView("welcome").build();
    }

//    @GetMapping("/routes")
//    public ModelAndView listRoutes() {
//        return new RouteView().list();
//    }

    @GetMapping("/voyages")
    public ModelAndView listVoyages() {
        return new TransporterCabView("voyage-list").build();
    }

    @GetMapping("/about")
    public ModelAndView about() {
        return new TransporterCabView("about").build();
    }
}
