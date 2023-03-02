package info.getbus.servebus.route.persistence.mappers;

import info.getbus.servebus.route.model.CompactRoute;
import info.getbus.servebus.route.model.RoundRoute;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RouteRound;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteMapper {
    List<CompactRoute> selectCompactRoutesByUsername(@Param("username") String username);
    RoundRoute selectRoundRouteById(@Param("id") long id);
    RouteRound selectByIdWithRoundRoute(@Param("routeId") long id, @Param("rounRouteId") long roundRouteId);
    Route selectById(@Param("id") long id);
    Route selectShallowById(@Param("id") long id);

    String selectLockOwner(@Param("id") Long id);
    String selectLockOwnerForUpdate(@Param("id") Long id);
    void insertLocked(@Param("transporterAreaId") Long transporterAreaId, @Param("route") Route route, @Param("lockOwner") String lockOwner);
    void insertRoundRouteLocked(@Param("carrierAreaId") long carrierAreaId, @Param("name") String name, @Param("lockOwner") String lockOwner);
    void insertRoundRouteNode(@Param("roundRouteId") long roundRouteId, @Param("name") String name, @Param("lockOwner") String lockOwner);
    void update(@Param("route") Route route);
    void updateLockOwner(@Param("id") Long id, @Param("lockOwner") String lockOwner);
    void unlockRoute(@Param("id") Long id);
}
