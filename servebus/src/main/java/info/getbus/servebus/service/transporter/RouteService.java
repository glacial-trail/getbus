package info.getbus.servebus.service.transporter;

import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.model.route.PeriodicityPair;
import info.getbus.servebus.model.route.Route;

import javax.annotation.Nullable;
import java.util.List;

public interface RouteService {
    List<CompactRoute> list();
    Route acquireForEdit(long routeId);
    void cancelEdit(long routeId);
    @Nullable
    Route saveAndProceed(Route route);
    @Nullable
    PeriodicityPair getPeriodicityPair(long routeId);
    void savePeriodicity(PeriodicityPair pair);
}