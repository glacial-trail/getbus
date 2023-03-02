package info.getbus.servebus.web.mav;

import info.getbus.servebus.geo.address.persistence.CountriesRepository;
import info.getbus.servebus.web.dto.route.PeriodicityPairDTO;
import info.getbus.servebus.web.dto.route.RouteDTO;
import info.getbus.servebus.web.views.CarrierDashboardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class RouteView extends CarrierDashboardView {
    private final CountriesRepository countriesRepository;

    @Autowired
    public RouteView(CountriesRepository countriesRepository) {
        this.countriesRepository = countriesRepository;
    }

    public ModelAndView periodicity(PeriodicityPairDTO periodicity) {
        addObject("periodicity", periodicity);
        return periodicity();
    }
    public ModelAndView periodicity() {
        return page("periodicity");
    }

    public ModelAndView list(List<RoundRouteSummaryDTO> routes) {
        addObject("routes", routes);
        return list();
    }
    public ModelAndView list() {
        return page("list");
    }

    public ModelAndView edit(RouteDTO route) {
        addObject("viewMode", false);
        return route(route);

    }

    public ModelAndView view(RouteViewDTO route) {
        addObject("viewMode", true);
        return route(route);
    }

    private ModelAndView route(RouteViewDTO route) {
        addObject("countries", countriesRepository.getCodes());
        addObject("route", route);
        return page("edit");
    }
}
