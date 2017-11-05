package info.getbus.servebus.web.controllers;

import info.getbus.servebus.model.route.*;
import info.getbus.servebus.repository.CountriesRepository;
import info.getbus.servebus.service.transporter.RouteService;
import info.getbus.servebus.web.dto.route.PeriodicityPairDTO;
import info.getbus.servebus.web.dto.route.RouteDTO;
import info.getbus.servebus.web.mav.RouteView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
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
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/list")
    public ModelAndView listRoutes() {
        List<CompactRoute> routes = routeService.list();
        return new RouteView(routes).list();
    }

    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable("id") long id, @RequestParam(name = "direction", required = false)Direction direction)  {
        //TODO handle lock exception
        if (null == direction) {
            direction = Direction.F;
        }
        Route route = routeService.get(new RoutePartId(id, direction));
        RouteDTO dto = modelMapper.map(route, RouteDTO.class);
        return new RouteView(dto)
                .view();
    }

    @GetMapping("/create")
    public ModelAndView getCreate() {
        //TODO refactor view instantiation with country-list (maybe prototype & lookup method)
        return new RouteView(new RouteDTO()).withCountries(countriesRepository.getAll()).edit();
    }

    @PostMapping("/cancel")
    public ModelAndView cancel(@RequestParam("id") Long id) {
//      if (null != id && ...
//        TODO remove route by route.id if route partially saved? leave as is, indicate as uncomplete, now delete
        routeService.cancelEdit(id);
        return new RouteView().redirect().list();
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") long id) {
        //TODO handle lock exception
        RouteDTO route = modelMapper.map(routeService.acquireForEdit(id), RouteDTO.class);
        return new RouteView(route)
                .withCountries(countriesRepository.getAll())
                .edit();
    }

    @PostMapping( {"/save", "/edit/save"})
    public ModelAndView save(@ModelAttribute("route") @Validated RouteDTO dto, BindingResult errors) {
        List<String> cc = countriesRepository.getAll();
        if (errors.hasErrors()) {
            return new RouteView(dto).withCountries(cc).edit();
        }

        Route route = modelMapper.map(dto, Route.class);
        Route opposite = routeService.saveAndProceed(route);
        if (null == opposite) {
            return new RouteView().redirect().list();
        } else {
            RouteDTO oppositeDto = modelMapper.map(opposite, RouteDTO.class);
            return new RouteView(oppositeDto).withCountries(cc).edit();
        }
    }

    @PostMapping({"/back", "/edit/back"})
    public ModelAndView backToCreateRouteForward(@ModelAttribute("route") @Validated RouteDTO dto, BindingResult errors) {
//        TODO for future: save partially filled route as tmp (dto not validated)
//        TODO load forward part by id and return
        Route route;
        if (!errors.hasErrors()) {
            route = modelMapper.map(dto, Route.class);
            routeService.saveAndProceed(route); //TODO save later? no forget
        }
        //TODO do both actions in the same transaction?
        route = routeService.acquireForEdit(dto.getId());//TODO what if route dto has empty id?
        dto = modelMapper.map(route, RouteDTO.class);
        return new RouteView(dto).withCountries(countriesRepository.getAll()).edit();
    }

    @GetMapping("/{id}/periodicity")
    public ModelAndView getEditPeriodicity(@PathVariable("id") long routeId) {
        routeService.acquireLock(routeId);
        PeriodicityPair pair = routeService.getPeriodicityPair(routeId);
        PeriodicityPairDTO dto;
        if (null == pair) {
            dto = PeriodicityPairDTO.empty();
        } else {
            dto = conversionService.convert(pair, PeriodicityPairDTO.class);
        }
        return new RouteView(routeId).periodicity(dto);
    }

    @PostMapping("{id}/periodicity")
    public ModelAndView savePeriodicity(@ModelAttribute("periodicity")
                                        @Validated
                                        PeriodicityPairDTO periodicity,
                                        BindingResult errors) {
//        TODO validation
//        if (errors.hasErrors()) {
//            return new RouteView().redirect().list();
//        }
        PeriodicityPair pair = conversionService.convert(periodicity, PeriodicityPair.class);
        routeService.savePeriodicity(pair);
        return new RouteView().redirect().list();
    }

    @PostMapping("{id}/periodicity/cancel")
    public ModelAndView cancelEditPeriodicity(@PathVariable("id") long routeId) {
        routeService.releaseConsistent(routeId);
        return new RouteView().redirect().list();
    }

}
