package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.RoutePartId;
import info.getbus.servebus.model.security.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RouteMapperTest extends RouteAwareBaseTest {

    private long transporterAreaId2;

    @Before
    public final void setUpAdditionalTransporterArea() {
        transporterAreaId2 = transporterService.createBaseTransporterInfrastructure();
        insertUser(transporterAreaId, "username11");
        insertUser(transporterAreaId, "username14");
        insertUser(transporterAreaId2, "username24");
    }

    private void insertUser(long transporterAreaId, String username) {
        User user = newUser(username);
        userMapper.insert(user);
        transporterService.linkUserToArea(transporterAreaId, user, "ROLE");
    }

    @Test
    public void insertAndSelect() throws Exception {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        Route actualRoute = routeMapper.selectById(new RoutePartId(route.getId(), route.getDirection()));
        assertThatRoutesAreEqual(actualRoute, route);
        assertThat(actualRoute.getRoutePoints(), is(empty()));
    }

    @Test
    public void insertAndSelectWithPoints() throws Exception {
        //to use direction R insertPointsFor must act as PersistenceManager (probably out of score mapper test?)
        route.setDirection(Direction.F);
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        insertPointsFor(route, true);
        Route actualRoute = routeMapper.selectById(new RoutePartId(route.getId(), route.getDirection()));
        assertThatRoutesAreFullyEqual(actualRoute, route);
    }

    @Test
    public void selectCompactRoutesByUsername() throws Exception {
        class RouteContainer extends SelfContainer<Long, Route> {
            private Set<Long> editableRoutes = new HashSet<>();

            private RouteContainer() {
                super(Route::getId);
            }

            private Route getFor(CompactRoute compactRoute) {
                return getByKey(compactRoute.getId());
            }
            private void putEditable(Route route) {
                put(route);
                editableRoutes.add(route.getId());
            }
            private void putNonEditable(Route route) {
                put(route);
            }
            private boolean isEditable(CompactRoute compactRoute) {
                return editableRoutes.contains(compactRoute.getId());
            }
        }
        RouteContainer expContainer = new RouteContainer();

        Route route = insertRouteFor(transporterAreaId, user.getUsername());
        expContainer.putEditable(route);
        route = insertRouteFor(transporterAreaId, user.getUsername());
        expContainer.putEditable(route);
        route = insertRouteFor(transporterAreaId, "username14");
        expContainer.putNonEditable(route);
        route = insertRouteFor(transporterAreaId2, "username24");
        expContainer.putNonEditable(route);
        Long alienRoute = route.getId();
        route = insertRouteFor(transporterAreaId, null);
        expContainer.putEditable(route);

        List<CompactRoute> compactRoutes = routeMapper.selectCompactRoutesByUsername(user.getUsername());
        assertThat(compactRoutes, hasSize(4));
        assertTrue(compactRoutes.stream().noneMatch(cRoute -> alienRoute == cRoute.getId()));

        for (CompactRoute actual : compactRoutes) {
            Route expected = expContainer.getFor(actual);
            assertThat(actual.getName(), is(expected.getName()));
            assertThat(actual.getStartPoint(), is(nameOfFirstPoint(expected)));
            assertThat(actual.getEndPoint(), is(nameOfLstPoint(expected)));
            assertThat(actual.isEditable(), is(expContainer.isEditable(actual)));
        }
    }

    private Route insertRouteFor(long transporterAreaId, String username) {
        Route route = newRoute();
        routeMapper.insertLocked(transporterAreaId, route, username);
        insertPointsFor(route);
        return route;
    }

    private String nameOfLstPoint(Route expected) {
        return expected.getRoutePoints().getLast().getName();
    }

    private String nameOfFirstPoint(Route expected) {
        return expected.getRoutePoints().getFirst().getName();
    }

    @Test
    public void selectLockOwnerForUpdate() throws Exception {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        String actualOwner = routeMapper.selectLockOwnerForUpdate(route.getId());
        assertThat(actualOwner, is(user.getUsername()));
    }

    @Test
    public void update() throws Exception {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        Route expected = newRoute();
        expected.setId(route.getId());
        routeMapper.update(expected);
        Route actualRoute = routeMapper.selectById(new RoutePartId(route.getId(), expected.getDirection()));
        assertThatRoutesAreEqual(actualRoute, expected);
    }

    @Test
    public void updateLockOwner() throws Exception {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        String expectedOwner = "username2";
        userMapper.insert(newUser(expectedOwner));
        routeMapper.updateLockOwner(route.getId(), expectedOwner);
        String actualOwner = routeMapper.selectLockOwnerForUpdate(route.getId());
        assertThat(actualOwner, is(expectedOwner));
    }

    @Test
    public void unlockRoute() throws Exception {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        routeMapper.unlockRoute(route.getId());
        String actualOwner = routeMapper.selectLockOwnerForUpdate(route.getId());
        assertThat(actualOwner, is(nullValue()));
    }

    private void assertThatRoutesAreEqual(Route actual, Route expected) {
        assertThatObjectsAreEqualUsingFields(actual, expected,
                "id", "name", "basePrice", "baseSeatsQty", "startSales", "salesDepth");
    }
    private void assertThatRoutesAreFullyEqual(Route actual, Route expected) {
        assertThatRoutesAreEqual(actual, expected);
        assertThatObjectsAreEqualUsingFields(actual, expected,"direction");
        new DoubleFor<>(actual.getRoutePoints(), expected.getRoutePoints()).iterate(
                (act, exp) -> {
                    assertThatPointsAreEqual(act, exp);
                    assertThatPointsDataAreEqual(act, exp);
                });
    }
}