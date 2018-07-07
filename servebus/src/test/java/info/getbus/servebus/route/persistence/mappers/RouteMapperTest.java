package info.getbus.servebus.route.persistence.mappers;

import info.getbus.servebus.model.security.User;
import info.getbus.servebus.route.model.CompactRoute;
import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RoutePartId;
import info.getbus.test.util.SelfContainer;
import info.getbus.utils.collect.DoubleFor;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static info.getbus.test.util.Assertions.assertThatObjectsAreEqualUsingFields;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class RouteMapperTest extends RouteAwarePersistenceBaseTest {

    private long transporterAreaId2;

    @Before
    public final void setUpAdditionalTransporterArea() {
        transporterAreaId2 = transporterService.createInitialTransporterInfrastructure();
        insertUser(transporterAreaId, "username11");
        insertUser(transporterAreaId, "username14");
        insertUser(transporterAreaId2, "username24");
    }

    private void insertUser(long transporterAreaId, String username) {
        User user = newUser(username);
        userMapper.insert(user);
        transporterService.linkUserToArea(transporterAreaId, user, "ROLE");
    }

    //TODO do we really need route without bus stops? test fails due to INNER JOIN
    @Test
    public void insertAndSelect() throws Exception {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        Route actualRoute = routeMapper.selectShallowById(new RoutePartId(route.getId(), route.getDirection()));
        assertThatRoutesAreEqual(actualRoute, route);
        assertThat(actualRoute.getStops(), is(empty()));
    }

    @Test
    public void insertAndSelectWithStops() throws Exception {
        //to use direction R insertStopsFor must act as PersistenceManager (probably out of score mapper test?)
        route = newRouteWithPersistedTopology();
        route.setDirection(Direction.F);
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        insertStopsFor(route, true);
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

        Route route = fullyCreateAndInsertRouteFor(transporterAreaId, user.getUsername());
        expContainer.putEditable(route);
        route = fullyCreateAndInsertRouteFor(transporterAreaId, user.getUsername());
        expContainer.putEditable(route);
        route = fullyCreateAndInsertRouteFor(transporterAreaId, "username14");
        expContainer.putNonEditable(route);
        route = fullyCreateAndInsertRouteFor(transporterAreaId2, "username24");
        Long alienRoute = route.getId();
        route = fullyCreateAndInsertRouteFor(transporterAreaId, null);
        expContainer.putEditable(route);

        List<CompactRoute> compactRoutes = routeMapper.selectCompactRoutesByUsername(user.getUsername());
        assertThat(compactRoutes, hasSize(4));
        assertThat(compactRoutes.stream().noneMatch(cRoute -> alienRoute == cRoute.getId()), is(true));

        for (CompactRoute actual : compactRoutes) {
            Route expected = expContainer.getFor(actual);
            assertThat(actual.getName(), is(expected.getName()));
            assertThat(actual.getStartStop(), is(nameOfFirstStop(expected)));
            assertThat(actual.getEndStop(), is(nameOfLastStop(expected)));
            assertThat(actual.isEditable(), is(expContainer.isEditable(actual)));
        }
    }

    private Route fullyCreateAndInsertRouteFor(long transporterAreaId, String username) {
        Route route = newRouteWithPersistedTopology();
        routeMapper.insertLocked(transporterAreaId, route, username);
        insertStopsFor(route);
        return route;
    }

    private String nameOfLastStop(Route expected) {
        return expected.getLastStop().getName();
    }

    private String nameOfFirstStop(Route expected) {
        return expected.getFirstStop().getName();
    }

    @Test
    public void selectLockOwnerForUpdate() throws Exception {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        String actualOwner = routeMapper.selectLockOwnerForUpdate(route.getId());
        assertThat(actualOwner, is(user.getUsername()));
    }

    @Test
    public void selectLockOwner() throws Exception {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        String actualOwner = routeMapper.selectLockOwner(route.getId());
        assertThat(actualOwner, is(user.getUsername()));
    }

    @Test
    public void update() throws Exception {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        Route expected = newRoute();
        expected.setId(route.getId());
        routeMapper.update(expected);
        Route actualRoute = routeMapper.selectShallowById(new RoutePartId(route.getId(), expected.getDirection()));
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
        new DoubleFor<>(actual.getStops(), expected.getStops()).iterate(
                (act, exp) -> {
                    assertThatStopsAreEqual(act, exp);
//       TODO?             assertReflectionEquals(addr, actAddress);
                    assertThatObjectsAreEqualUsingFields(act.getAddress(), exp.getAddress(),
                            "id", "countryCode", "adminArea1", "city", "streetBuilding", "street", "building", "zip", "utcOffset");
                    assertThatStopsDataAreEqual(act, exp);
                });
    }
}