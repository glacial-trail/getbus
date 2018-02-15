package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.WayPoint;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static info.getbus.servebus.model.route.Direction.R;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class WayPointMapperTest extends RouteAwarePersistenceBaseTest {
    @Autowired
    private WayPointMapper wayPointMapper;

    @Before
    public final void setUpRoute() {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
    }

    @Test
    public void selectPoints() throws Exception {
        insertPointsFor(route);
        Deque<WayPoint> points = wayPointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThat(points, hasSizeOf(route.getWayPoints()));
        new DoubleFor<>(route.getWayPoints(), points).iterate(
                (actual, expected) -> assertThat(expected.getId(), is(actual.getId())));
    }

    @Test
    public void insertAndSelectPoint() throws Exception {
        WayPoint expected = route.getWayPoints().getFirst();
        wayPointMapper.insert(route.getId(), expected, 0);
        assertThat(expected.getId(), is(notNullValue()));

        Deque<WayPoint> points = wayPointMapper.selectRoutePointsWithData(route.getId(), route.getDirection());
        assertThat(points, hasSize(1));
        WayPoint actual = points.getFirst();
        assertThatPointsAreEqual(actual, expected);
        assertThatRoutePointDataIsNull(actual);
    }

    @Test
    public void updatePoint() throws Exception {
        WayPoint point = route.getWayPoints().getFirst();
        wayPointMapper.insert(route.getId(), point, 0);
        WayPoint expected = route.getWayPoints().getLast();
        expected.setId(point.getId());
        wayPointMapper.update(expected);
        Deque<WayPoint> points = wayPointMapper.selectRoutePointsWithData(route.getId(), route.getDirection());
        assertThatPointsAreEqual(points.getFirst(), expected);
    }

    @Test
    public void insertAndSelectData() throws Exception {
        testInsert((wp, direction) -> wayPointMapper.insertData(wp, direction));
    }

    @Test
    public void insertAndSelectDataIfNonExist() throws Exception {
        testInsert((wp, direction) -> wayPointMapper.insertDataIfNonExist(wp, direction));
    }

    private void testInsert(BiFunction<WayPoint, Direction, Integer> insert) {
        WayPoint expected = route.getWayPoints().getFirst();
        wayPointMapper.insert(route.getId(), expected, 0);
        int upc = insert.apply(expected, R);
        assertThat(upc, is(1));
        Deque<WayPoint> points = wayPointMapper.selectRoutePointsWithData(route.getId(), R);
        assertThatPointsAreEqual(points.getFirst(), expected);
        assertThatPointsDataAreEqual(points.getFirst(), expected);
    }

    @Test
    public void selectPointDataNegative() throws Exception {
        WayPoint point = route.getWayPoints().getFirst();
        wayPointMapper.insert(route.getId(), point, 0);
        wayPointMapper.insertDataIfNonExist(point, R);
        Deque<WayPoint> points = wayPointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThatPointsAreEqual(points.getFirst(), point);
        assertThatRoutePointDataIsNull(points.getFirst());
    }

    @Test
    public void insertInsertDataIfNonExist() throws Exception {
        WayPoint point = route.getWayPoints().getFirst();
        wayPointMapper.insert(route.getId(), point, 0);
        int rowsInserted = wayPointMapper.insertDataIfNonExist(point, R);
        assertThat(rowsInserted, is(1));
        WayPoint toUpdate = route.getWayPoints().getLast();
        toUpdate.setId(point.getId());
        rowsInserted = wayPointMapper.insertDataIfNonExist(toUpdate, R);
        assertThat(rowsInserted, is(0));
    }

    @Test
    public void updatePointData() throws Exception {
        WayPoint expectedPoint = route.getWayPoints().getFirst();
        wayPointMapper.insert(route.getId(), expectedPoint, 0);
        wayPointMapper.insertDataIfNonExist(expectedPoint, Direction.F);
        WayPoint expectedData = route.getWayPoints().getLast();
        expectedData.setId(expectedPoint.getId());
        wayPointMapper.updateData(expectedData, Direction.F);
        Deque<WayPoint> points = wayPointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThatPointsAreEqual(points.getFirst(), expectedPoint);
        assertThatPointsDataAreEqual(points.getFirst(), expectedData);
    }

    @Test
    public void updatePointSequence() throws Exception {
        WayPoint uno = route.getWayPoints().getFirst();
        WayPoint dos = route.getWayPoints().getLast();
        wayPointMapper.insert(route.getId(), uno, 1);
        wayPointMapper.insert(route.getId(), dos, 2);
        wayPointMapper.updateSequence(uno.getId(), -1);
        wayPointMapper.updateSequence(dos.getId(), 1);
        wayPointMapper.updateSequence(uno.getId(), 2);
        Deque<WayPoint> actual = wayPointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThat(actual.getFirst().getId(), is(dos.getId()));
        assertThat(actual.getLast().getId(), is(uno.getId()));
    }

    @Test
    public void existInconsistentPoints() throws Exception {
        WayPoint point = route.getWayPoints().getFirst();
        wayPointMapper.insert(route.getId(), point, 0);
        wayPointMapper.insertDataIfNonExist(point, Direction.F);
        assertThat(wayPointMapper.existInconsistentRoutePoints(route.getId()), is(true));
    }

    @Test
    public void existInconsistentPointsNegative() throws Exception {
        WayPoint point = route.getWayPoints().getFirst();
        wayPointMapper.insert(route.getId(), point, 0);
        wayPointMapper.insertDataIfNonExist(point, Direction.F);
        wayPointMapper.insertDataIfNonExist(point, R);
        assertThat(wayPointMapper.existInconsistentRoutePoints(route.getId()), is(false));
    }

    private void assertThatRoutePointDataIsNull(WayPoint point) {
        assertThat(point.getArrival(), nullValue());
        assertThat(point.getDeparture(), nullValue());
        assertThat(point.getDistance(), nullValue());
        assertThat(point.getTripTime(), nullValue());
    }

    @Test
    public void deleteNotIn() throws Exception {
        Route otherRoute = insertNewRoute();
        insertPointsFor(route);
        Set<Long> notDeletedIdsExp = new HashSet<>();
        int i = 1;
        for (WayPoint wp : route.getWayPoints()) {
            if (1 == i || 3 == i || 6 == i) {
                notDeletedIdsExp.add(wp.getId());
            }
            i++;
        }
        int deleted = wayPointMapper.deleteNotIn(route.getId(), notDeletedIdsExp);
        assertThat(deleted, is(route.getWayPoints().size() - notDeletedIdsExp.size()));

        Deque<WayPoint> points = wayPointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThat(points, hasSizeOf(notDeletedIdsExp));
        Set<Long> notDeletedIdsAct = points.stream().map(WayPoint::getId).collect(Collectors.toSet());
        assertThat(notDeletedIdsAct, containsInAnyOrder(notDeletedIdsExp));

        points = wayPointMapper.selectRoutePointsWithData(otherRoute.getId(), Direction.F);
        assertThat(points, hasSizeOf(otherRoute.getWayPoints()));
    }

    private Route insertNewRoute() {
        Route otherRoute = newRoute();
        routeMapper.insertLocked(transporterAreaId, otherRoute, user.getUsername());
        insertPointsFor(otherRoute);
        return otherRoute;
    }

    public static <E> org.hamcrest.Matcher<java.util.Collection<? extends E>> hasSizeOf(Collection c) {
        return hasSize(c.size());
    }

    public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(Iterable<T> items) {
        List<Matcher<? super T>> matchers = new ArrayList<>();
        for (T item : items) {
            matchers.add(equalTo(item));
        }
        return new IsIterableContainingInAnyOrder<>(matchers);
    }
}