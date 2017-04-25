package info.getbus.servebus.web.controllers;

import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.repository.CountriesRepository;
import info.getbus.servebus.service.transporter.RouteService;
import info.getbus.servebus.web.mav.RouteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

// TODO post-redirect-get
@Controller
@RequestMapping("/tr/routes/")
public class RouteManagementController {
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private RouteService routeService;

    @GetMapping("/list")
    public ModelAndView listRoutes() {
        List<CompactRoute> routes = routeService.listRoutes();
        return new RouteView(routes).list();
    }

    @GetMapping("/create")
    public ModelAndView getCreate() {
        //TODO refactor view instantiation with country-list (maybe prototype & lookup method)
        return new RouteView(new Route()).withCountries(countriesRepository.getAll()).edit();
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
                             Route route,
                             BindingResult errors) {
        List<String> cc = countriesRepository.getAll();
        if (errors.hasErrors()) {
            return new RouteView(route).withCountries(cc).edit();
        }
        if (Direction.F == route.getDirection()) {
//          TODO save route
//          TODO reverse route
            route.setDirection(Direction.R);
            route.setId(99L);
            return new RouteView().withCountries(cc).edit();
        } else {
//          TODO save route
            return new RouteView().redirect().list();
        }
    }

    @PostMapping("/back")
    public ModelAndView backToCreateRouteForward(@ModelAttribute("route") Route route) {
//        TODO for future: save partially filled route as tmp (dto not validated)
//        TODO load forward part by id and return
        route.setDirection(Direction.F);
        return new RouteView().edit();
    }
}
