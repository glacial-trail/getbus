package info.getbus.servebus.route.persistence.mappers;

 import info.getbus.servebus.geo.address.Address;
import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RoutePartId;
import info.getbus.servebus.route.model.WayPoint;
import info.getbus.servebus.topology.StopPlace;
import info.getbus.utils.collect.DoubleFor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Deque;
import java.util.List;

import static info.getbus.servebus.route.model.Direction.F;
import static info.getbus.servebus.route.model.Direction.R;
import static info.getbus.test.harmcrest.Matchers.hasSizeOf;
import static info.getbus.test.util.Assertions.assertThatObjectsAreEqualUsingFields;
import static info.getbus.test.util.Assertions.assertThatObjectsAreNotEqualUsingFields;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class WayPointMapperTest extends RouteAwarePersistenceBaseTest {
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private WayPointMapper wayPointMapper;


    @Test
    public void stopAddress() throws Exception {
        Route route = newRoute();
        WayPoint stop = route.getFirstStop();
        Address address = stop.getAddress();
        addressMapper.insert(address);
        addressMapper.insertL10n(address);
        StopPlace place = newPlace(address);
        stopPlaceMapper.insert(place);
        stop.setStopId(place.getId());
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        wayPointMapper.upsert(stop);

        Route actualRoute = routeMapper.selectById(new RoutePartId(route.getId(), route.getDirection()));
        Address actualAddress = actualRoute.getFirstStop().getAddress();
        assertThat(actualAddress, notNullValue());
        assertReflectionEquals(address, actualAddress);
//        assertThat(actAddress, recursivelyEqualsTo(addr)); string equals issue
    }

    @Before
    public final void setUpRoute() {
        route = newRouteWithPersistedTopology();
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
    }

    @Test
    public void selectPoints() throws Exception {
        insertStopsFor(route);
        Deque<WayPoint> points = wayPointMapper.selectFullWayPoints(route.getId(), F);
        assertThat(points, hasSizeOf(route.getWayPoints()));
        new DoubleFor<>(route.getWayPoints(), points).iterate(
                (actual, expected) -> assertThat(expected.getStopId(), is(actual.getStopId())));
    }

    @Test
    public void fullInsertAndSelectPoint() throws Exception {
        WayPoint expected = route.getFirstStop();
        wayPointMapper.upsert(expected);
        wayPointMapper.upsertLength(expected, route.getDirection());
        wayPointMapper.upsertTimetable(expected, route.getDirection());
        //TODO delete wayPointMapper.selectFullWayPoints
        Deque<WayPoint> points = wayPointMapper.selectFullWayPoints(route.getId(), route.getDirection());
        assertThat(points, hasSize(1));
        WayPoint actual = points.getFirst();
        assertReflectionEquals(expected, actual);
//        assertThat(actual, recursivelyEqualsTo(expected));
    }

    private abstract class AbstractPersistTestTemplate {
        String eqFields[];
        AbstractPersistTestTemplate(String... fields) {
            this.eqFields = new String[fields.length + 2];
            this.eqFields[0] = "routeId";
            this.eqFields[1] = "stopId";
            System.arraycopy(fields, 0, this.eqFields, 2, fields.length);
        }
        abstract void persistData(WayPoint stop, Direction direction);
        abstract void test();
    }
    private abstract class InsertTestTemplate extends AbstractPersistTestTemplate {
        InsertTestTemplate(String... fields) {
            super(fields);
        }
        @Override
        void test() {
            WayPoint expected = route.getFirstStop();
            wayPointMapper.upsert(expected);
            persistData(expected, route.getDirection());
            Route routeWithInsertedStop = routeMapper.selectById(routeId());
            assertThat(routeWithInsertedStop.getWayPoints(), hasSize(1));
            WayPoint actual = routeWithInsertedStop.getFirstStop();
            assertThatObjectsAreEqualUsingFields(actual, expected, eqFields);
        }
    }
    private abstract class UpdateTestTemplate extends AbstractPersistTestTemplate {
        String neqFields[];
        UpdateTestTemplate(String... fields) {
            super(fields);
            neqFields = fields;
        }
        @Override
        void test() {
            WayPoint stopV1 = route.getFirstStop();
            wayPointMapper.upsert(stopV1);
            persistData(stopV1, route.getDirection());
            Route routeAfterInsert = routeMapper.selectById(routeId());

            WayPoint expected = route.getLastStop();
            expected.setStopId(stopV1.getStopId());
            persistData(expected, route.getDirection());
            Route routeAfterUpdate = routeMapper.selectById(routeId());
            assertThat(routeAfterUpdate.getWayPoints(), hasSize(1));
            WayPoint actualStopAfterUpdate = routeAfterUpdate.getFirstStop();

            assertThatObjectsAreEqualUsingFields(actualStopAfterUpdate, expected, eqFields);
            assertThatObjectsAreNotEqualUsingFields(actualStopAfterUpdate, routeAfterInsert.getFirstStop(), neqFields);
        }
    }

    @Test
    public void insert() throws Exception {
        new InsertTestTemplate("name", "sequence") {
            void persistData(WayPoint stop, Direction direction) { }
        }.test();
    }

    @Test
    public void update() throws Exception {
        new UpdateTestTemplate() {
            int cc;
            void persistData(WayPoint stop, Direction direction) {
               if (++cc == 2) {
                   wayPointMapper.upsert(stop);
               }
            }
        }.test();
    }

    @Test
    public void insertLength() throws Exception {
        new InsertTestTemplate("distance") {
            void persistData(WayPoint stop, Direction direction) {
                wayPointMapper.upsertLength(stop, direction);
            }
        }.test();
    }

    @Test
    public void updateLength() throws Exception {
        new UpdateTestTemplate("distance") {
            void persistData(WayPoint stop, Direction direction) {
                wayPointMapper.upsertLength(stop, direction);
            }
        }.test();
    }

    @Test
    public void insertTimetable() throws Exception {
        new InsertTestTemplate("arrival", "departure", "tripTime") {
            void persistData(WayPoint stop, Direction direction) {
                wayPointMapper.upsertTimetable(stop, direction);
            }
        }.test();
    }

    @Test
    public void updateTimetable() throws Exception {
        new UpdateTestTemplate("arrival", "departure", "tripTime") {
            void persistData(WayPoint stop, Direction direction) {
                wayPointMapper.upsertTimetable(stop, direction);
            }
        }.test();
    }

    @Test
    public void selectStopDataNegative() throws Exception {
        WayPoint stop = route.getFirstStop();
        wayPointMapper.upsert(stop);
        wayPointMapper.upsertLength(stop, R);
        Deque<WayPoint> stops = wayPointMapper.selectFullWayPoints(route.getId(), F);
        assertThatStopsAreEqual(stops.getFirst(), stop);
        assertThatRouteStopDataIsNull(stops.getFirst());
    }

    private void assertThatRouteStopDataIsNull(WayPoint point) {
        assertThat(point.getArrival(), nullValue());
        assertThat(point.getDeparture(), nullValue());
        assertThat(point.getDistance(), nullValue());
        assertThat(point.getTripTime(), nullValue());
    }

    @Test
    public void existInconsistentStops() throws Exception {
        WayPoint stop = route.getFirstStop();
        wayPointMapper.upsert(stop);
        wayPointMapper.upsertLength(stop, F);
        assertThat(wayPointMapper.existInconsistentRoutePoints(route.getId()), is(true));
    }

    @Test
    public void existInconsistentStopsNegative() throws Exception {
        WayPoint stop = route.getFirstStop();
        wayPointMapper.upsert(stop);
        wayPointMapper.upsertLength(stop, F);
        wayPointMapper.upsertLength(stop, R);
        assertThat(wayPointMapper.existInconsistentRoutePoints(route.getId()), is(false));
    }

    @Test
    public void negateSequence() throws Exception {
        insertStopsFor(route);
        wayPointMapper.negateSequence(route.getId());
        List<WayPoint> actual = routeMapper.selectById(routeId()).getWayPoints();
        assertThat(actual.stream().anyMatch(s -> s.getSequence() < 0), is(true));
    }

    @Test
    public void deleteOutOfRange() throws Exception {
        route.setDirection(F);
        insertStopsFor(route);
        wayPointMapper.deleteOutOfRange(route.getId(), 4);
        List<WayPoint> actual = routeMapper.selectById(routeId()).getWayPoints();
        assertThat(actual, hasSize(4));
        new DoubleFor<>(actual, route.getWayPoints().subList(0, 4)).iterate(this::assertThatStopsAreEqual);
    }

    private RoutePartId routeId() {
        return new RoutePartId(route.getId(), route.getDirection());
    }
}