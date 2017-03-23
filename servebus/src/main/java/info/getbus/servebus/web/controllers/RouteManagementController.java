package info.getbus.servebus.web.controllers;

import info.getbus.servebus.model.web.dto.transporter.route.Direction;
import info.getbus.servebus.model.web.dto.transporter.route.RouteDTO;
import info.getbus.servebus.web.mav.RouteView;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

// TODO post-redirect-get
@Controller
@RequestMapping("/tr/routes/")
public class RouteManagementController {

    @GetMapping("/list")
    public ModelAndView listRoutes() {
        return new RouteView().list();
    }

    @GetMapping("/create")
    public ModelAndView getCreate() {
        return new RouteView(new RouteDTO()).edit();
    }

    @PostMapping("/cancel")
    public ModelAndView cancel(@RequestParam("id") Long id) {
//      if (null != id && ...
//        TODO remove route by route.id if route partially saved.
        return new RouteView().redirect().list();
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("route")
                             @Validated
                             RouteDTO route,
                             BindingResult errors) {
        if (errors.hasErrors()) {
            return new RouteView(route).edit();
        }
        if (Direction.F == route.getDirection()) {
//          TODO save route
//          TODO reverse route
            route.setDirection(Direction.R);
            route.setId(99L);
            return new RouteView().edit();
        } else {
//          TODO save route
            return new RouteView().redirect().list();
        }
    }

    @PostMapping("/back")
    public ModelAndView backToCreateRouteForward(@ModelAttribute("route") RouteDTO route) {
//        TODO for future: save partially filled route as tmp (dto not validated)
//        TODO load forward part by id and return
        route.setDirection(Direction.F);
        return new RouteView().edit();
    }
}
