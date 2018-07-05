package info.getbus.servebus.route.persistence.mappers;

import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.WayPoint;
import org.apache.ibatis.annotations.Param;

import java.util.Deque;

public interface WayPointMapper {
    //TODO remove
    Deque<WayPoint> selectFullWayPoints(@Param("routeId") Long routeId, @Param("direction") Direction direction);
    void upsert(@Param("stop") WayPoint point);
    void upsertLength(@Param("stop") WayPoint point, @Param("direction") Direction direction);
    void upsertTimetable(@Param("stop") WayPoint point, @Param("direction") Direction direction);
    int negateSequence(@Param("routeId") long routeId);
    int deleteOutOfRange(@Param("routeId") long routeId, @Param("last") int last);
    /*will be*/@Deprecated
    boolean existInconsistentRoutePoints(@Param("routeId") Long routeId);
}
