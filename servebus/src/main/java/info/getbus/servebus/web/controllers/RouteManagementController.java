package info.getbus.servebus.web.controllers;

import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.model.route.PeriodicityPair;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.RoutePartId;
import info.getbus.servebus.service.transporter.RouteService;
import info.getbus.servebus.web.dto.route.PeriodicityPairDTO;
import info.getbus.servebus.web.dto.route.RouteDTO;
import info.getbus.servebus.web.mav.Redirect;
import info.getbus.servebus.web.mav.RouteView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/t/routes/")
public class RouteManagementController {
    @Autowired
    private RouteService routeService;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private ModelMapper modelMapper;

    @Lookup
    public RouteView view() { return null; }

    private Redirect path(String path) {
        return new Redirect("/t/routes/", path);
    }
    
    @GetMapping("/list")
    public ModelAndView listRoutes() {
        List<CompactRoute> routes = routeService.list();
        return view().list(routes);
    }

    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") long id,
                             @RequestParam(name = "direction", required = false) Direction direction)  {
        //TODO handle lock exception
        if (null == direction) {
            direction = Direction.F;
        }
        Route route = routeService.get(new RoutePartId(id, direction));
        RouteDTO dto = modelMapper.map(route, RouteDTO.class);
        return view().view(dto);
    }

    @GetMapping("/create")
    public ModelAndView getCreate() {
        return view().edit(new RouteDTO());
    }

    @PostMapping({"/cancel", "/edit/cancel"})
    public ModelAndView cancel(@RequestParam("id") Long id) {
//      if (null != id && ...
//        TODO remove route by route.id if route partially saved? leave as is, indicate as uncomplete, now delete
        routeService.cancelEdit(id);
        return path("/list").redirect();
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") long id,
                             @RequestParam(name = "direction", defaultValue = "F") Direction direction) {
        //TODO handle lock exception
        RouteDTO route = modelMapper.map(routeService.acquireForEdit(new RoutePartId(id, direction)), RouteDTO.class);
        return view().edit(route);
    }

    @PostMapping({"/save", "/edit/save"})
    public ModelAndView save(@RequestParam(name = "finish", defaultValue = "false") boolean finish,
                             @ModelAttribute("route") @Validated RouteDTO dto,
                             BindingResult errors) {
        if (errors.hasErrors()) {
            return view().edit(dto);
        }

        Route route = modelMapper.map(dto, Route.class);
        boolean done = routeService.saveAndCheckConsistency(route, finish);
        if (finish && done) {
            return path("/list").redirect();
        } else {
            return path("/edit/").with(builder -> builder
                    .path(route.getId().toString())
                    .queryParam("direction", route.oppositeDirection())
            ).redirect();
        }
    }

    @PostMapping({"/back", "/edit/back"})
    public ModelAndView backToCreateRouteForward(@ModelAttribute("route") @Validated RouteDTO dto,
                                                 BindingResult errors) {
//        TODO for future: save partially filled route as tmp (dto not validated)
//        TODO load forward part by id and return
        Route route;
        if (!errors.hasErrors()) {
            route = modelMapper.map(dto, Route.class);
            routeService.saveAndCheckConsistency(route, false); //TODO save later? no forget
        }
        return path("/edit/").with(builder -> builder
                .path(dto.getId().toString())
                .queryParam("direction", Direction.F)
        ).redirect();//TODO what if route dto has empty id?
    }

    @GetMapping("/{id}/periodicity")
    public ModelAndView getEditPeriodicity(@PathVariable("id") long routeId) {
        routeService.acquireLock(routeId);
        PeriodicityPair pair = routeService.getPeriodicityPair(routeId);
        PeriodicityPairDTO dto;
        if (null == pair) {
            dto = PeriodicityPairDTO.empty(routeId);
        } else {
            dto = conversionService.convert(pair, PeriodicityPairDTO.class);
        }
        return view().periodicity(dto);
    }

    @PostMapping("{id}/periodicity")
    public ModelAndView savePeriodicity(@ModelAttribute("periodicity")
                                        @Validated
                                        PeriodicityPairDTO periodicity,
                                        BindingResult errors) {
//        TODO validation
//        if (errors.hasErrors()) {
//            path("/list").redirect();
//        }
        PeriodicityPair pair = conversionService.convert(periodicity, PeriodicityPair.class);
        routeService.savePeriodicity(pair);
        return path("/list").redirect();
    }

    @PostMapping("{id}/periodicity/cancel")
    public ModelAndView cancelEditPeriodicity(@PathVariable("id") long routeId) {
        routeService.releaseConsistent(routeId);
        return path("/list").redirect();
    }

}
