package info.getbus.servebus.route.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RouteRound extends Route {
    private RoundRouteSummary roundRouteSummary;

    @Override
    protected Route newRoute() {
        return  new RouteRound();
    }

    @Override
    protected Route copy(Route route) {
        Route r = super.copy(route);
        RouteRound rr = (RouteRound) r;
        rr.setRoundRouteSummary(roundRouteSummary);
        return rr;
    }
}
