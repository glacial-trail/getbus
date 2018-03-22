package info.getbus.servebus.route.model;

public class PeriodicityPair {
    private long routeId;
    private RoutePeriodicity forward;
    private RoutePeriodicity reverse;

    public PeriodicityPair(long routeId,  RoutePeriodicity forward, RoutePeriodicity reverse) {
        this.routeId = routeId;
        this.forward = forward;
        this.reverse = reverse;
    }

    public long getRouteId() {
        return routeId;
    }

    public RoutePeriodicity getForward() {
        return forward;
    }

    public RoutePeriodicity getReverse() {
        return reverse;
    }
}
