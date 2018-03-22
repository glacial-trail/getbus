package info.getbus.servebus.route;

import info.getbus.servebus.route.model.CompactRoute;
import info.getbus.servebus.route.model.PeriodicityPair;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RoutePartId;

import javax.annotation.Nullable;
import java.util.List;

public interface RouteService {
    List<CompactRoute> list();
    Route get(RoutePartId id);
    Route acquireForEdit(RoutePartId partId);
    void acquireLock(long routeId);
    void cancelEdit(long routeId);
    void releaseConsistent(long routeId);
    boolean saveAndCheckConsistency(Route route, boolean unlockIfConsistent);
    @Nullable
    PeriodicityPair getPeriodicityPair(long routeId);
    void savePeriodicity(PeriodicityPair pair);
}