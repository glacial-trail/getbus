package info.getbus.servebus.web.mav;

import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.web.views.TransporterCabView;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


public class RouteView extends TransporterCabView {
    public static final String ROUTE_PREFIX = "route/";
    public static final String ROUTE_NAME = "route";
    public static final String ROUTES_NAME = "routes";
    public static final String COUNTRIES_NAME = "countries";

    private Route route;
    private List<CompactRoute> routes;
    private List<String> countries;

    public RouteView() {
    }

    public RouteView(Route route) {
        this.route = route;
    }

    public RouteView(List<CompactRoute> routes) {
        this.routes = routes;
    }

    public RouteView withCountries(List<String> countries) {
        this.countries = countries;
        return this;
    }

    @Override
    public ModelAndView build() {
        ModelAndView mav = super.build();
        if (null != route) {
            mav.addObject(ROUTE_NAME, route);
        }
        if (null != routes) {
            mav.addObject(ROUTES_NAME, routes);
        }
        if (null != countries) {
            mav.addObject(COUNTRIES_NAME, countries);
        }
        return mav;
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
        public ModelAndView list() {
            return new ModelAndView("redirect:list");
        }
    }
}
