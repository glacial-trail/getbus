package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.route.CompactRoute;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteMapper {
    List<CompactRoute> selectCompactRoutesByUsername(@Param("username") String username);
}
