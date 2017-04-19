package info.getbus.servebus.web.mav;

import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.model.web.dto.transporter.route.RouteDTO;
import info.getbus.servebus.web.views.TransporterCabView;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


public class RouteView extends TransporterCabView {
    public static final String ROUTE_PREFIX = "route/";
    public static final String ROUTE_NAME = "route";
    public static final String ROUTES_NAME = "routes";

    private RouteDTO route;
    private List<CompactRoute> routes;

    public RouteView() {
    }

    public RouteView(RouteDTO route) {
        this.route = route;
    }

    public RouteView(List<CompactRoute> routes) {
        this.routes = routes;
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
