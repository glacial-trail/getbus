package info.getbus.servebus.route.persistence.mappers;

import info.getbus.servebus.route.model.Periodicity;
import info.getbus.servebus.route.model.RoutePeriodicity;
import info.getbus.servebus.route.model.RoutePartId;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoutePeriodicityMapper {
    List<RoutePeriodicity> selectByRouteId(@Param("id") Long id);
    void insert(@Param("routeId") RoutePartId routeId, @Param("periodicity") Periodicity periodicity);
    void update(@Param("routeId") Long routeId, @Param("periodicity") Periodicity periodicity);
}
