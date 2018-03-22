package info.getbus.servebus.route.persistence.managers;

import info.getbus.servebus.route.model.Periodicity;
import info.getbus.servebus.route.model.PeriodicityPair;
import info.getbus.servebus.route.model.RoutePartId;

import javax.annotation.Nullable;

public interface RoutePeriodicityPersistenceManager {
    @Nullable
    PeriodicityPair getPair(long routeId);
    void save(RoutePartId routeId, Periodicity periodicity);
}
