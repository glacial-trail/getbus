package info.getbus.servebus.service.transporter;

import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.model.route.Route;

import java.util.List;

public interface RouteService {
    List<CompactRoute> list();
    Route acquireForEdit(long routeId);
    void cancelEdit(long routeId);
    Route saveAndProceed(Route route);
}