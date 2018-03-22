package info.getbus.servebus.route.persistence.mappers;

import info.getbus.servebus.route.model.CompactRoute;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RoutePartId;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteMapper {
    List<CompactRoute> selectCompactRoutesByUsername(@Param("username") String username);
    Route selectById(@Param("cid") RoutePartId id);

    String selectLockOwner(@Param("id") Long id);
    String selectLockOwnerForUpdate(@Param("id") Long id);
    void insertLocked(@Param("transporterAreaId") Long transporterAreaId, @Param("route") Route route, @Param("lockOwner") String lockOwner);
    void update(@Param("route") Route route);
    void updateLockOwner(@Param("id") Long id, @Param("lockOwner") String lockOwner);
    void unlockRoute(@Param("id") Long id);
}
