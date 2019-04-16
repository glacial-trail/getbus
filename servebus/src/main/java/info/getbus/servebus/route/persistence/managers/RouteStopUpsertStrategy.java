package info.getbus.servebus.route.persistence.managers;

import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.RouteStop;

public interface RouteStopUpsertStrategy {
    void upsert(RouteStop stop);
    void upsertLength(RouteStop stop, Direction direction);
    void upsertTimetable(RouteStop stop, Direction direction);

}
