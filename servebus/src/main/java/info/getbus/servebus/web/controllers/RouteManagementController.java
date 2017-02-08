package info.getbus.servebus.web.controllers;

import info.getbus.servebus.model.web.dto.transporter.route.RouteDTO;
import info.getbus.servebus.web.views.TransporterCabView;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/tr/")
public class RouteManagementController {

    @GetMapping("/create-route")
    public ModelAndView getCreate() {
        return new TransporterCabView("edit-route");
    }

    @PostMapping("/create-route")
    public String createRoute(@ModelAttribute("route") RouteDTO route) {
        return "redirect:/tr/routes";
    }
}
