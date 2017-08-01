package info.getbus.servebus.model.route;

import info.getbus.servebus.model.periodicity.Periodicity;


public class RoutePeriodicity {
    private RoutePartId routePartId;
    private Periodicity periodicity;

    public RoutePeriodicity(){ }

    public RoutePeriodicity(RoutePartId routePartId, Periodicity periodicity) {
        this.routePartId = routePartId;
        this.periodicity = periodicity;
    }

    public RoutePartId getRoutePartId() {
        return routePartId;
    }

    public void setRoutePartId(RoutePartId routePartId) {
        this.routePartId = routePartId;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }
}
