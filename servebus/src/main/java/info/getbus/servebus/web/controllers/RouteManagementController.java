package info.getbus.servebus.web.controllers;

import info.getbus.servebus.geo.GeoService;
import info.getbus.servebus.geo.address.Address;
import info.getbus.servebus.route.RouteService;
import info.getbus.servebus.route.model.CompactRoute;
import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.PeriodicityPair;
import info.getbus.servebus.route.model.RoundRouteSummary;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RouteCompositeId;
import info.getbus.servebus.route.model.RouteRound;
import info.getbus.servebus.route.model.RouteStop;
import info.getbus.servebus.topology.StopPlace;
import info.getbus.servebus.topology.TopologyService;
import info.getbus.servebus.web.dto.route.PeriodicityPairDTO;
import info.getbus.servebus.web.dto.route.RouteDTO;
import info.getbus.servebus.web.dto.route.RouteViewDTO;
import info.getbus.servebus.web.mav.Forward;
import info.getbus.servebus.web.mav.Redirect;
import info.getbus.servebus.web.mav.RouteView;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
@RequiredArgsConstructor
public class RouteManagementController {
    private final ConversionService conversionService;
    private final ModelMapper modelMapper;
    private final GeoService geoService;
    private final TopologyService topologyService;
    private final RouteService routeService;

    @Lookup
    public RouteView view() { return null; }

    private Redirect path(String path) {
        return new Redirect("/t/routes/", path);
    }
    private Forward fpath(String path) {
        return new Forward("/t/routes/", path);
    }

    @GetMapping("/list")
    public ModelAndView listRoutes() {
        List<CompactRoute> routes = routeService.list();
        return view().list(routes);
    }

    @GetMapping("/view/{roundRouteId}/{routeId}")
    public ModelAndView view(@PathVariable("roundRouteId") long roundRouteId, @PathVariable("id") long routeId)  {
        //TODO handle lock exception
        RouteDTO dto;
        if ( 0 == roundRouteId) {
            Route route = routeService.get(routeId);
            dto = modelMapper.map(route, RouteDTO.class);

        } else {
            RouteRound roundRoute = routeService.get(new RouteCompositeId(routeId, roundRouteId));
            //TODO create modelmaperconfig
            dto = modelMapper.map(roundRoute, RouteDTO.class);
        }

//        Route route = routeService.get2(new RouteCompositeId(routeId,roundRouteId)); TODO try this
//        dto = modelMapper.map(route, RouteDTO.class);


        return view().view(dto);
    }

    @GetMapping("/create")
    public ModelAndView getCreate(@RequestParam(name = "from", required = false) Long routeId,
                                  @RequestParam(name = "ofRoundRoute", required = false) Long roundRouteId
    ) {
        if (null == roundRouteId && null == routeId) {
            return view().edit(new RouteViewDTO());
        }

        if (null != routeId) {
            Route origRoute = routeService.get2(new RouteCompositeId(routeId, null == roundRouteId ? 0 : roundRouteId));
            Route r = origRoute.newReverted();
            return view().edit(modelMapper.map(r, RouteViewDTO.class));

        }

        RouteRound retVal = new RouteRound();
            RoundRouteSummary roundRouteSummary = routeService.getRoundRouteSummary(roundRouteId);
            retVal.setRoundRouteSummary(roundRouteSummary);

        return view().edit(modelMapper.map(retVal, RouteViewDTO.class));
    }

    @PostMapping({"/cancel", "/edit/cancel"})
    public ModelAndView cancel(@RequestParam("id") Long id) {
//      if (null != id && ...
//        TODO remove route by route.id if route partially saved? leave as is, indicate as uncomplete, now delete
        routeService.cancelEdit(id);
        return path("/list").redirect();
    }

    @GetMapping("/edit/{roundRouteId}/{routeId}")
    public ModelAndView edit(@PathVariable("roundRouteId") long roundRouteId, @PathVariable("id") long routeId) {
        //TODO handle lock exception
        RouteDTO route = modelMapper.map(routeService.acquireForEdit(new RouteCompositeId(routeId, roundRouteId)), RouteDTO.class);
        return view().edit(route);
    }

    @PostMapping("/createRoundRoute")
    public ModelAndView createRoundRoute(@RequestParam(name = "firstRoute") long id,
                                         @RequestParam(name = "reverse", defaultValue = "false") boolean reverse) {
        long rrid = routeService.createRoundRouteFor(id);
        return path("/create").with(builder -> {
            builder.queryParam("ofRoundRoute", rrid);
            if (reverse) builder.queryParam("from", id);
        }).redirect();
    }

    @PostMapping({"/save", "/edit/save"})
    public ModelAndView save(@RequestParam(required = false) Long next,
                             @RequestParam(name = "new", defaultValue = "false")  boolean createnew,
                             @RequestParam(name = "rev", defaultValue = "false")  boolean rev,
                             @ModelAttribute("route") @Validated RouteDTO dto,
                             BindingResult errors) {
        if (errors.hasErrors()) {
            RoundRouteSummary roundRouteSummary = routeService.getRoundRouteSummary(roundRouteId);

            return view().edit(dto);
        }

        RouteRound route = modelMapper.map(dto, RouteRound.class);//todo model mapper
        //TODO move away following loop
        for (RouteStop stop : route.getStops()) {
            Address address = geoService.ensureSaved(stop.getAddress());//TODO accept address id from client?
            stop.setAddress(address);
            /* TODO
                alternative:
                accept stop id from client
                if null - save new stop
                else pass to route service (or if noone use stop place - change name in case of different)
                route service save name in local route stop
                same with address?
            * */
            StopPlace stopPlace = topologyService.ensureSaved(new StopPlace(stop));
            stop.setStopId(stopPlace.getId());
        }

        boolean done = routeService.saveAndCheckConsistency(route, finish);

        if (null != next) {

        }





        if (!createnew) {
            return path("/list").redirect();
        } else {
            return fpath("/edit/").with(builder -> { builder
                    .path(route.getId())
                    .queryParam("direction", route.oppositeDirection());
                    if (rev) builder.queryParam("")
                    if (rev) builder.queryParam("")
            }
            ).redirect();
        }
    }

    @PostMapping({"/back", "/edit/back"})
    public ModelAndView backToCreateRouteForward(@ModelAttribute("route") @Validated RouteDTO dto,
                                                 BindingResult errors) {
//        TODO for future: save partially filled route as tmp (dto not validated)
//        TODO load forward part by id and return
        Route route;
        if (!errors.hasErrors() && false) { //disabled flow TODO do
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
