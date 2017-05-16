package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.route.RoutePoint;
import info.getbus.servebus.model.route.Direction;
import org.apache.ibatis.annotations.Param;

import java.util.Deque;

public interface RoutePointMapper {
    Deque<RoutePoint> selectRoutePoints(@Param("routeId") Long routeId, @Param("direction") Direction direction);
    void insert(@Param("routeId") Long routeId, @Param("point") RoutePoint point, @Param("sequence") int sequence);
    void update(@Param("point")RoutePoint point);
    void insertDataIfNonExist(@Param("point") RoutePoint point, @Param("direction") Direction direction);
    void updateData(@Param("point") RoutePoint point, @Param("direction") Direction direction);
    void updateSequence(@Param("id") Long id, @Param("sequence") Integer sequence);
    boolean existInconsistentRoutePoints(@Param("routeId") Long routeId);
}
