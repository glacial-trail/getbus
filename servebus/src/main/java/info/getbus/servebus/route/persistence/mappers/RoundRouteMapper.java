package info.getbus.servebus.route.persistence.mappers;

import info.getbus.servebus.route.model.CompactRoute;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoundRouteMapper {
    List<CompactRoute> selectCompactRoutesByUsername(@Param("username") String username);
}
