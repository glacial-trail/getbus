package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.model.route.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteMapper {
    List<CompactRoute> selectCompactRoutesByUsername(@Param("username") String username);

    Route selectById(@Param("id") long id);

    String selectLockOwnerForUpdate(@Param("id") Long id);
    void insertLocked(@Param("transporterAreaId") Long transporterAreaId, @Param("route") Route route, @Param("lockOwner") String lockOwner);
    void update(@Param("route") Route route);
    void updateLockOwner(@Param("id") Long id, @Param("lockOwner") String lockOwner);
    void unlockRoute(@Param("id") Long id);
}
