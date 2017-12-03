package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.model.route.RoutePoint;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Deque;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RoutePointMapperTest extends RouteAwareBaseTest {
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
        assertThat(points, hasSize(route.getRoutePoints().size()));
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
    public void insertAndSelectDataIfNonExist() throws Exception {
        RoutePoint expected = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), expected, 0);
        routePointMapper.insertDataIfNonExist(expected, Direction.R);
        Deque<RoutePoint> points = routePointMapper.selectRoutePointsWithData(route.getId(), Direction.R);
        assertThatPointsAreEqual(points.getFirst(), expected);
        assertThatPointsDataAreEqual(points.getFirst(), expected);
    }

    @Test
    public void selectPointDataNegative() throws Exception {
        RoutePoint point = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), point, 0);
        routePointMapper.insertDataIfNonExist(point, Direction.R);
        Deque<RoutePoint> points = routePointMapper.selectRoutePointsWithData(route.getId(), Direction.F);
        assertThatPointsAreEqual(points.getFirst(), point);
        assertThatRoutePointDataIsNull(points.getFirst());
    }

    @Test
    public void insertInsertDataIfNonExist() throws Exception {
        RoutePoint point = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), point, 0);
        int rowsInserted = routePointMapper.insertDataIfNonExist(point, Direction.R);
        assertThat(rowsInserted, is(1));
        RoutePoint toUpdate = route.getRoutePoints().getLast();
        toUpdate.setId(point.getId());
        rowsInserted = routePointMapper.insertDataIfNonExist(toUpdate, Direction.R);
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
        assertTrue(routePointMapper.existInconsistentRoutePoints(route.getId()));
    }

    @Test
    public void existInconsistentPointsNegative() throws Exception {
        RoutePoint point = route.getRoutePoints().getFirst();
        routePointMapper.insert(route.getId(), point, 0);
        routePointMapper.insertDataIfNonExist(point, Direction.F);
        routePointMapper.insertDataIfNonExist(point, Direction.R);
        assertFalse(routePointMapper.existInconsistentRoutePoints(route.getId()));
    }

    private void assertThatRoutePointDataIsNull(RoutePoint point) {
        assertThat(point.getArrival(), nullValue());
        assertThat(point.getDeparture(), nullValue());
        assertThat(point.getDistance(), nullValue());
        assertThat(point.getTripTime(), nullValue());
    }
}