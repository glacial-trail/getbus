package info.getbus.servebus.route.persistence.managers;

import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.RouteStop;
import info.getbus.servebus.route.persistence.mappers.RouteStopMapperOld;
import info.getbus.servebus.route.annotation.SelectUpdateInsert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@SelectUpdateInsert
@RequiredArgsConstructor
public class RouteStopUpsertStrategySelectUpdateInsert implements RouteStopUpsertStrategy {
    private final RouteStopMapperOld mapper;

    @Override
    public void upsert(RouteStop stop) {
        if (mapper.existsStop(stop)) {
            mapper.updateStop(stop);
        } else {
            mapper.insertStop(stop);
        }
    }

    @Override
    public void upsertLength(RouteStop stop, Direction direction) {
        if (mapper.existsLength(stop, direction)) {
            mapper.updateLength(stop, direction);
        } else {
            mapper.insertLength(stop, direction);
        }
    }

    @Override
    public void upsertTimetable(RouteStop stop, Direction direction) {
        if (mapper.existsLength(stop, direction)) {
            mapper.updateLength(stop, direction);
        } else {
            mapper.insertLength(stop, direction);
        }
    }
}
