package info.getbus.servebus.service.transporter;

import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.model.route.PeriodicityPair;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.RoutePartId;

import javax.annotation.Nullable;
import java.util.List;

public interface RouteService {
    List<CompactRoute> list();
    Route get(RoutePartId id);
    Route acquireForEdit(long routeId);
    void acquireLock(long routeId);
    void cancelEdit(long routeId);
    void releaseConsistent(long routeId);
    @Nullable
    Route saveAndProceed(Route route);
    @Nullable
    PeriodicityPair getPeriodicityPair(long routeId);
    void savePeriodicity(PeriodicityPair pair);
}