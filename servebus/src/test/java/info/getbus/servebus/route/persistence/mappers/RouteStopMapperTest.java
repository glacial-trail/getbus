package info.getbus.servebus.route.persistence.mappers;

 import info.getbus.servebus.geo.address.Address;
import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RoutePartId;
 import info.getbus.servebus.route.model.RouteStop;
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

public class RouteStopMapperTest extends RouteAwarePersistenceBaseTest {
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private RouteStopMapper routeStopMapper;


    @Test
    public void stopAddress() throws Exception {
        Route route = newRoute();
        RouteStop stop = route.getFirstStop();
        Address address = stop.getAddress();
        addressMapper.insert(address);
        addressMapper.insertL10n(address);
        StopPlace place = newPlace(address);
        stopPlaceMapper.insert(place);
        stop.setStopId(place.getId());
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
        routeStopMapper.upsert(stop);

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
    public void selectStops() throws Exception {
        insertStopsFor(route);
        Deque<RouteStop> stops = routeStopMapper.selectFullWayPoints(route.getId(), F);
        assertThat(stops, hasSizeOf(route.getStops()));
        new DoubleFor<>(route.getStops(), stops).iterate(
                (actual, expected) -> assertThat(expected.getStopId(), is(actual.getStopId())));
    }

    @Test
    public void fullInsertAndSelectStop() throws Exception {
        RouteStop expected = route.getFirstStop();
        routeStopMapper.upsert(expected);
        routeStopMapper.upsertLength(expected, route.getDirection());
        routeStopMapper.upsertTimetable(expected, route.getDirection());
        //TODO delete routeStopMapper.selectFullWayPoints
        Deque<RouteStop> stops = routeStopMapper.selectFullWayPoints(route.getId(), route.getDirection());
        assertThat(stops, hasSize(1));
        RouteStop actual = stops.getFirst();
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
        abstract void persistData(RouteStop stop, Direction direction);
        abstract void test();
    }
    private abstract class InsertTestTemplate extends AbstractPersistTestTemplate {
        InsertTestTemplate(String... fields) {
            super(fields);
        }
        @Override
        void test() {
            RouteStop expected = route.getFirstStop();
            routeStopMapper.upsert(expected);
            persistData(expected, route.getDirection());
            Route routeWithInsertedStop = routeMapper.selectById(routeId());
            assertThat(routeWithInsertedStop.getStops(), hasSize(1));
            RouteStop actual = routeWithInsertedStop.getFirstStop();
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
            RouteStop stopV1 = route.getFirstStop();
            routeStopMapper.upsert(stopV1);
            persistData(stopV1, route.getDirection());
            Route routeAfterInsert = routeMapper.selectById(routeId());

            RouteStop expected = route.getLastStop();
            expected.setStopId(stopV1.getStopId());
            persistData(expected, route.getDirection());
            Route routeAfterUpdate = routeMapper.selectById(routeId());
            assertThat(routeAfterUpdate.getStops(), hasSize(1));
            RouteStop actualStopAfterUpdate = routeAfterUpdate.getFirstStop();

            assertThatObjectsAreEqualUsingFields(actualStopAfterUpdate, expected, eqFields);
            assertThatObjectsAreNotEqualUsingFields(actualStopAfterUpdate, routeAfterInsert.getFirstStop(), neqFields);
        }
    }

    @Test
    public void insert() throws Exception {
        new InsertTestTemplate("name", "sequence") {
            void persistData(RouteStop stop, Direction direction) { }
        }.test();
    }

    @Test
    public void update() throws Exception {
        new UpdateTestTemplate() {
            int cc;
            void persistData(RouteStop stop, Direction direction) {
               if (++cc == 2) {
                   routeStopMapper.upsert(stop);
               }
            }
        }.test();
    }

    @Test
    public void insertLength() throws Exception {
        new InsertTestTemplate("distance") {
            void persistData(RouteStop stop, Direction direction) {
                routeStopMapper.upsertLength(stop, direction);
            }
        }.test();
    }

    @Test
    public void updateLength() throws Exception {
        new UpdateTestTemplate("distance") {
            void persistData(RouteStop stop, Direction direction) {
                routeStopMapper.upsertLength(stop, direction);
            }
        }.test();
    }

    @Test
    public void insertTimetable() throws Exception {
        new InsertTestTemplate("arrival", "departure", "tripTime") {
            void persistData(RouteStop stop, Direction direction) {
                routeStopMapper.upsertTimetable(stop, direction);
            }
        }.test();
    }

    @Test
    public void updateTimetable() throws Exception {
        new UpdateTestTemplate("arrival", "departure", "tripTime") {
            void persistData(RouteStop stop, Direction direction) {
                routeStopMapper.upsertTimetable(stop, direction);
            }
        }.test();
    }

    @Test
    public void selectStopDataNegative() throws Exception {
        RouteStop stop = route.getFirstStop();
        routeStopMapper.upsert(stop);
        routeStopMapper.upsertLength(stop, R);
        Deque<RouteStop> stops = routeStopMapper.selectFullWayPoints(route.getId(), F);
        assertThatStopsAreEqual(stops.getFirst(), stop);
        assertThatRouteStopDataIsNull(stops.getFirst());
    }

    private void assertThatRouteStopDataIsNull(RouteStop stop) {
        assertThat(stop.getArrival(), nullValue());
        assertThat(stop.getDeparture(), nullValue());
        assertThat(stop.getDistance(), nullValue());
        assertThat(stop.getTripTime(), nullValue());
    }

    @Test
    public void existInconsistentStops() throws Exception {
        RouteStop stop = route.getFirstStop();
        routeStopMapper.upsert(stop);
        routeStopMapper.upsertLength(stop, F);
        assertThat(routeStopMapper.existInconsistentRoutePoints(route.getId()), is(true));
    }

    @Test
    public void existInconsistentStopsNegative() throws Exception {
        RouteStop stop = route.getFirstStop();
        routeStopMapper.upsert(stop);
        routeStopMapper.upsertLength(stop, F);
        routeStopMapper.upsertLength(stop, R);
        assertThat(routeStopMapper.existInconsistentRoutePoints(route.getId()), is(false));
    }

    @Test
    public void negateSequence() throws Exception {
        insertStopsFor(route);
        routeStopMapper.negateSequence(route.getId());
        List<RouteStop> actual = routeMapper.selectById(routeId()).getStops();
        assertThat(actual.stream().anyMatch(s -> s.getSequence() < 0), is(true));
    }

    @Test
    public void deleteOutOfRange() throws Exception {
        route.setDirection(F);
        insertStopsFor(route);
        routeStopMapper.deleteOutOfRange(route.getId(), 4);
        List<RouteStop> actual = routeMapper.selectById(routeId()).getStops();
        assertThat(actual, hasSize(4));
        new DoubleFor<>(actual, route.getStops().subList(0, 4)).iterate(this::assertThatStopsAreEqual);
    }

    private RoutePartId routeId() {
        return new RoutePartId(route.getId(), route.getDirection());
    }
}