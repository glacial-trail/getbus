package info.getbus.servebus.route;

import info.getbus.servebus.route.model.RoundRouteSummary;
import info.getbus.servebus.route.model.CompactRoute;
import info.getbus.servebus.route.model.PeriodicityPair;
import info.getbus.servebus.route.model.RoundRoute;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RouteCompositeId;
import info.getbus.servebus.route.model.RouteRound;

import javax.annotation.Nullable;
import java.util.List;

public interface RouteService {
    List<CompactRoute> list();
    RouteRound get(RouteCompositeId routeCompositeId);
    Route get(long routeId);
    RoundRoute getRR(long rrId);
    public Route get2(RouteCompositeId routeCompositeId);
    Route acquireForEdit(long routeId);
    Route acquireForEdit(RouteCompositeId id);
    void acquireLock(long routeId);
    void cancelEdit(long routeId);
    void releaseConsistent(long routeId);
    boolean saveAndCheckConsistency(Route route, boolean unlockIfConsistent);
    @Nullable
    PeriodicityPair getPeriodicityPair(long routeId);
    void savePeriodicity(PeriodicityPair pair);

    long createRoundRouteFor(long routeId);


    RoundRouteSummary getRoundRouteSummary(long id);
}