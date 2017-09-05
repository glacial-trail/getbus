package info.getbus.servebus.persistence.managers;

import info.getbus.servebus.model.periodicity.Periodicity;
import info.getbus.servebus.model.route.PeriodicityPair;
import info.getbus.servebus.model.route.RoutePartId;

import javax.annotation.Nullable;

public interface RoutePeriodicityPersistenceManager {
    @Nullable
    PeriodicityPair getPair(long routeId);
    void save(RoutePartId routeId, Periodicity periodicity);
}
