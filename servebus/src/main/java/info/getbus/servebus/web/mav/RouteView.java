package info.getbus.servebus.web.mav;

import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.repository.CountriesRepository;
import info.getbus.servebus.web.dto.route.PeriodicityPairDTO;
import info.getbus.servebus.web.dto.route.RouteDTO;
import info.getbus.servebus.web.views.TransporterCabView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component
@Scope("prototype")
public class RouteView extends TransporterCabView {
    public static final String ROUTES_URL = "/routes";
    public static final String FTL_ROUTE_PREFIX = "route/";

    @Autowired
    private CountriesRepository countriesRepository;

    @Override
    public ModelAndView build() {
        super.build();
//      mav.addObject("tzList", ZoneId.getAvailableZoneIds());
        return mav;
    }

    public ModelAndView periodicity(PeriodicityPairDTO periodicity) {
        mav.addObject("periodicity", periodicity);
        return periodicity();
    }
    public ModelAndView periodicity() {
        return page("periodicity").build();
    }

    public ModelAndView list(List<CompactRoute> routes) {
        mav.addObject("routes", routes);
        return list();
    }
    public ModelAndView list() {
        return page("list").build();
    }

    public ModelAndView edit(RouteDTO route) {
        mav.addObject("viewMode", false);
        return mainView(route);

    }

    public ModelAndView view(RouteDTO route) {
        mav.addObject("viewMode", true);
        return mainView(route);
    }

    private ModelAndView mainView(RouteDTO route) {
        mav.addObject("countries", countriesRepository.getAll());
        mav.addObject("route", route);
        return page("edit").build();
    }

    @Override
    protected String getCabPage() {
        return FTL_ROUTE_PREFIX + super.getCabPage();
    }

    public Redirect redirect() {
        return new Redirect();
    }

    public class Redirect {
        public static final String REDIRECT = "redirect:";
        public ModelAndView list() {
            return new ModelAndView(REDIRECT + CAB_URL + ROUTES_URL + "/list");
        }
    }
}
