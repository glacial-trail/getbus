package info.getbus.servebus.route.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static info.getbus.servebus.route.model.Direction.F;
import static info.getbus.servebus.route.model.Direction.R;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class RouteTest {

    @Test
    void setId() {
        Route route = makeRoute();
        assertThat (
                route
                        .getWayPoints()
                        .stream()
                        .allMatch(stop -> null == stop.getRouteId())
                , is(true)
        );
        route.setId(42L);
        assertThat(
                route
                        .getWayPoints()
                        .stream()
                        .allMatch(stop -> 42 == stop.getRouteId())
                , is(true)
        );
    }

    @Test
    void setWayPoints() {
        List<WayPoint> stops = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            stops.add(new WayPoint());
        }
        Route route = new Route();
        route.setId(42L);
        route.setWayPoints(stops);

        assertThat(
                route
                        .getWayPoints()
                        .stream()
                        .allMatch(stop -> 42 == stop.getRouteId())
                , is(true)
        );
    }

    @Test
    void isForward_positive() {
        Route route = new Route();
        route.setDirection(F);
        assertThat(route.isForward(), is(true));
    }
    @Test
    void isForward_negative() {
        Route route = new Route();
        route.setDirection(R);
        assertThat(route.isForward(), is(false));
    }

    @Test
    void oppositeDirection() {
        Route route = new Route();
        route.setDirection(R);
        assertThat(route.oppositeDirection(), is(F));
    }


    @Test
    void getFirstStop() {
        Route route = makeRoute();
        WayPoint first = route.getFirstStop();
        assertThat(first, notNullValue());
        assertThat(first.getSequence(), is(1));
    }

    @Test
    void getLastStop() {
        Route route = makeRoute(F, 5);
        WayPoint last = route.getLastStop();
        assertThat(last, notNullValue());
        assertThat(last.getSequence(), is(5));
    }

    @Test
    void getRoutePointsInNaturalOrderF() {
        Route route = makeRoute(F);
        int i = 1;
        for (WayPoint wp : route.getRoutePointsInNaturalOrder()) {
            assertThat(wp.getSequence(), is(i++));
        }
    }

    @Test
    void getRoutePointsInNaturalOrderR() {
        Route route = makeRoute(R);
        int i = 4;
        for (WayPoint stop : route.getRoutePointsInNaturalOrder()) {
            assertThat(stop.getSequence(), is(i--));
        }
    }

    private Route makeRoute() {
        return makeRoute(R, 4);
    }

    private Route makeRoute(Direction direction) {
        return makeRoute(direction, 4);
    }

    private Route makeRoute(Direction direction, int length) {
        List<WayPoint> stops = new LinkedList<>();
        for (int i = 1; i <= length ; i++) {
            WayPoint stop = new WayPoint();
            stop.setSequence(i);
            stops.add(stop);
        }
        Route route = new Route();
        route.setDirection(direction);
        route.setWayPoints(stops);
        return route;
    }

    @Test
    void isDistanceFulfilled() {
        Route route = new Route();
        for (int i = 0; i < 4; i++) {
            route.getWayPoints().add(newStopWithDistance());
        }
        assertThat(route.isDistanceFulfilled(), is(true));
    }

    @Test
    void isDistanceFulfilledNegativeFull() {
        Route route = new Route();
        for (int i = 0; i < 4; i++) {
            route.getWayPoints().add(new WayPoint());
        }
        assertThat(route.isDistanceFulfilled(), is(false));
    }

    @Test
    void isDistanceFulfilledNegative() {
        Route route = new Route();
        route.getWayPoints().add(newStopWithDistance());
        route.getWayPoints().add(newStopWithDistance());
        route.getWayPoints().add(new WayPoint());
        route.getWayPoints().add(newStopWithDistance());
        assertThat(route.isDistanceFulfilled(), is(false));
    }

    private WayPoint newStopWithDistance() {
        WayPoint stop = new WayPoint();
        stop.setDistance(4);
        return stop;
    }
}