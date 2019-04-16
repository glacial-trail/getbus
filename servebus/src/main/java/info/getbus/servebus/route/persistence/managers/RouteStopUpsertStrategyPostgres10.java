package info.getbus.servebus.route.persistence.managers;

import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.RouteStop;
import info.getbus.servebus.route.persistence.mappers.RouteStopMapper;
import info.getbus.servebus.route.annotation.Postgres10Upsert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Postgres10Upsert
@RequiredArgsConstructor
public class RouteStopUpsertStrategyPostgres10 implements RouteStopUpsertStrategy {
    private final RouteStopMapper mapper;

    @Override
    public void upsert(RouteStop stop) {
        mapper.upsert(stop);
    }

    @Override
    public void upsertLength(RouteStop stop, Direction direction) {
        mapper.upsertLength(stop, direction);
    }

    @Override
    public void upsertTimetable(RouteStop stop, Direction direction) {
        mapper.upsertTimetable(stop, direction);
    }
}
