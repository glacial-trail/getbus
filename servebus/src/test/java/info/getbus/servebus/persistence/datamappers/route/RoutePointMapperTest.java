package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.RoutePoint;
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

public class RoutePointMapperTest extends RouteAwarePersistenceBaseTest {
    @Autowired
    private RoutePointMapper routePointMapper;

    @Before
    public final void setUpRoute() {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
    }

    @Test
    public void selectPoints() throws Exception {
        insertPointsFor(route);
        Deque<RoutePoint> points = routePointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThat(points, hasSizeOf(route.getRoutePoints()));
        new DoubleFor<>(route.getRoutePoints(), points).iterate(
                (actual, expected) -> assertThat(expected.getId(), is(actual.getId())));
    }

    @Test
    public void insertAndSelectPoint() throws Exception {
        RoutePoint expected = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), expected, 0);
        assertThat(expected.getId(), is(notNullValue()));

        Deque<RoutePoint> points = routePointMapper.selectRoutePointsWithData(route.getId(), route.getDirection());
        assertThat(points, hasSize(1));
        RoutePoint actual = points.getFirst();
        assertThatPointsAreEqual(actual, expected);
        assertThatRoutePointDataIsNull(actual);
    }

    @Test
    public void updatePoint() throws Exception {
        RoutePoint point = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), point, 0);
        RoutePoint expected = route.getRoutePoints().getLast();
        expected.setId(point.getId());
        routePointMapper.update(expected);
        Deque<RoutePoint> points = routePointMapper.selectRoutePointsWithData(route.getId(), route.getDirection());
        assertThatPointsAreEqual(points.getFirst(), expected);
    }

    @Test
    public void insertAndSelectData() throws Exception {
        testInsert((wp, direction) -> routePointMapper.insertData(wp, direction));
    }

    @Test
    public void insertAndSelectDataIfNonExist() throws Exception {
        testInsert((wp, direction) -> routePointMapper.insertDataIfNonExist(wp, direction));
    }

    private void testInsert(BiFunction<RoutePoint, Direction, Integer> insert) {
        RoutePoint expected = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), expected, 0);
        int upc = insert.apply(expected, R);
        assertThat(upc, is(1));
        Deque<RoutePoint> points = routePointMapper.selectRoutePointsWithData(route.getId(), R);
        assertThatPointsAreEqual(points.getFirst(), expected);
        assertThatPointsDataAreEqual(points.getFirst(), expected);
    }

    @Test
    public void selectPointDataNegative() throws Exception {
        RoutePoint point = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), point, 0);
        routePointMapper.insertDataIfNonExist(point, R);
        Deque<RoutePoint> points = routePointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThatPointsAreEqual(points.getFirst(), point);
        assertThatRoutePointDataIsNull(points.getFirst());
    }

    @Test
    public void insertInsertDataIfNonExist() throws Exception {
        RoutePoint point = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), point, 0);
        int rowsInserted = routePointMapper.insertDataIfNonExist(point, R);
        assertThat(rowsInserted, is(1));
        RoutePoint toUpdate = route.getRoutePoints().getLast();
        toUpdate.setId(point.getId());
        rowsInserted = routePointMapper.insertDataIfNonExist(toUpdate, R);
        assertThat(rowsInserted, is(0));
    }

    @Test
    public void updatePointData() throws Exception {
        RoutePoint expectedPoint = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), expectedPoint, 0);
        routePointMapper.insertDataIfNonExist(expectedPoint, Direction.F);
        RoutePoint expectedData = route.getRoutePoints().getLast();
        expectedData.setId(expectedPoint.getId());
        routePointMapper.updateData(expectedData, Direction.F);
        Deque<RoutePoint> points = routePointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThatPointsAreEqual(points.getFirst(), expectedPoint);
        assertThatPointsDataAreEqual(points.getFirst(), expectedData);
    }

    @Test
    public void updatePointSequence() throws Exception {
        RoutePoint uno = route.getRoutePoints().getFirst();
        RoutePoint dos = route.getRoutePoints().getLast();
        routePointMapper.insert(route.getId(), uno, 1);
        routePointMapper.insert(route.getId(), dos, 2);
        routePointMapper.updateSequence(uno.getId(), -1);
        routePointMapper.updateSequence(dos.getId(), 1);
        routePointMapper.updateSequence(uno.getId(), 2);
        Deque<RoutePoint> actual = routePointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThat(actual.getFirst().getId(), is(dos.getId()));
        assertThat(actual.getLast().getId(), is(uno.getId()));
    }

    @Test
    public void existInconsistentPoints() throws Exception {
        RoutePoint point = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), point, 0);
        routePointMapper.insertDataIfNonExist(point, Direction.F);
        assertThat(routePointMapper.existInconsistentRoutePoints(route.getId()), is(true));
    }

    @Test
    public void existInconsistentPointsNegative() throws Exception {
        RoutePoint point = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), point, 0);
        routePointMapper.insertDataIfNonExist(point, Direction.F);
        routePointMapper.insertDataIfNonExist(point, R);
        assertThat(routePointMapper.existInconsistentRoutePoints(route.getId()), is(false));
    }

    private void assertThatRoutePointDataIsNull(RoutePoint point) {
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
        for (RoutePoint wp : route.getRoutePoints()) {
            if (1 == i || 3 == i || 6 == i) {
                notDeletedIdsExp.add(wp.getId());
            }
            i++;
        }
        int deleted = routePointMapper.deleteNotIn(route.getId(), notDeletedIdsExp);
        assertThat(deleted, is(route.getRoutePoints().size() - notDeletedIdsExp.size()));

        Deque<RoutePoint> points = routePointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThat(points, hasSizeOf(notDeletedIdsExp));
        Set<Long> notDeletedIdsAct = points.stream().map(RoutePoint::getId).collect(Collectors.toSet());
        assertThat(notDeletedIdsAct, containsInAnyOrder(notDeletedIdsExp));

        points = routePointMapper.selectRoutePointsWithData(otherRoute.getId(), Direction.F);
        assertThat(points, hasSizeOf(otherRoute.getRoutePoints()));
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