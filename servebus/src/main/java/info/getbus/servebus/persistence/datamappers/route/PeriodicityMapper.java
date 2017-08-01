package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.periodicity.Periodicity;
import info.getbus.servebus.model.route.RoutePeriodicity;
import info.getbus.servebus.model.route.RoutePartId;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PeriodicityMapper {
    List<RoutePeriodicity> selectByRouteId(@Param("id") Long id);
    void insert(@Param("routeId") RoutePartId routeId, @Param("periodicity") Periodicity periodicity);
    void update(@Param("periodicity") Periodicity periodicity);
}
