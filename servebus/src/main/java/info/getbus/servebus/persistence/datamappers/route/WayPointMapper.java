package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.model.route.WayPoint;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Deque;

public interface WayPointMapper {
    Deque<WayPoint> selectRoutePointsWithData(@Param("routeId") Long routeId, @Param("direction") Direction direction);
    void insert(@Param("routeId") Long routeId, @Param("point") WayPoint point, @Param("sequence") int sequence);
    void update(@Param("point")WayPoint point);
    int insertData(@Param("point") WayPoint point, @Param("direction") Direction direction);
    int insertDataIfNonExist(@Param("point") WayPoint point, @Param("direction") Direction direction);
    void updateData(@Param("point") WayPoint point, @Param("direction") Direction direction);
    void updateSequence(@Param("id") Long id, @Param("sequence") Integer sequence);
    boolean existInconsistentRoutePoints(@Param("routeId") Long routeId);
    int deleteNotIn(@Param("routeId") Long routeId, @Param("ids") Collection<Long> ids);
}
