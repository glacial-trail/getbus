package info.getbus.servebus.web.mav;

import info.getbus.servebus.model.route.*;
import info.getbus.servebus.web.dto.route.PeriodicityPairDTO;
import info.getbus.servebus.web.dto.route.RouteDTO;
import info.getbus.servebus.web.views.TransporterCabView;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;


public class RouteView extends TransporterCabView {
    public static final String ROUTES_URL = "/routes";
    public static final String ROUTE_PREFIX = "route/";
    public static final String ROUTE_NAME = "route";
    public static final String ROUTE_ID_NAME = "routeId";
    public static final String ROUTES_NAME = "routes";
    public static final String COUNTRIES_NAME = "countries";
    public static final String TZ_LIST = "tzList";
    public static final String PERIODICITY_NAME = "periodicity";

    private Long routeId;
    private RouteDTO route;
    private List<CompactRoute> routes;
    private List<String> countries;
    private Collection<String> tzList;
    private PeriodicityPairDTO periodicity;

    public RouteView() {
    }

    public RouteView(Long routeId) {
        this.routeId = routeId;
    }

    public RouteView(RouteDTO route) {
        this.route = route;
    }

    public RouteView(List<CompactRoute> routes) {
        this.routes = routes;
    }

    public RouteView withCountries(List<String> countries) {
        this.countries = countries;
        return this;
    }

    public RouteView withTZlist(Collection<String> tzList) {
        this.tzList = tzList;
        return this;
    }

    @Override
    public ModelAndView build() {
        ModelAndView mav = super.build();
        if (null != routeId) {
            mav.addObject(ROUTE_ID_NAME, routeId);
        }
        if (null != route) {
            mav.addObject(ROUTE_NAME, route);
        }
        if (null != routes) {
            mav.addObject(ROUTES_NAME, routes);
        }
        if (null != countries) {
            mav.addObject(COUNTRIES_NAME, countries);
        }
        if (null != tzList) {
            mav.addObject(TZ_LIST, tzList);
        }
        if (null != periodicity) {
            mav.addObject(PERIODICITY_NAME, periodicity);
        }
        return mav;
    }

    public ModelAndView periodicity(PeriodicityPairDTO periodicity) {
        this.periodicity = periodicity;
        return periodicity();
    }
    public ModelAndView periodicity() {
        return page("periodicity").build();
    }

    public ModelAndView list() {
        return page("list").build();
    }

    public ModelAndView edit() {
        return page("edit").build();
    }

    @Override
    protected String getCabPage() {
        return ROUTE_PREFIX + super.getCabPage();
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
