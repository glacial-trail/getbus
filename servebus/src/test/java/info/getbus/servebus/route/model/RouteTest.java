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
                        .getStops()
                        .stream()
                        .allMatch(stop -> null == stop.getRouteId())
                , is(true)
        );
        route.setId(42L);
        assertThat(
                route
                        .getStops()
                        .stream()
                        .allMatch(stop -> 42 == stop.getRouteId())
                , is(true)
        );
    }

    @Test
    void setStops() {
        List<RouteStop> stops = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            stops.add(new RouteStop());
        }
        Route route = new Route();
        route.setId(42L);
        route.setStops(stops);

        assertThat(
                route
                        .getStops()
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
        RouteStop first = route.getFirstStop();
        assertThat(first, notNullValue());
        assertThat(first.getSequence(), is(1));
    }

    @Test
    void getLastStop() {
        Route route = makeRoute(F, 5);
        RouteStop last = route.getLastStop();
        assertThat(last, notNullValue());
        assertThat(last.getSequence(), is(5));
    }

    @Test
    void getRoutePointsInNaturalOrderF() {
        Route route = makeRoute(F);
        int i = 1;
        for (RouteStop stop : route.getRoutePointsInNaturalOrder()) {
            assertThat(stop.getSequence(), is(i++));
        }
    }

    @Test
    void getRoutePointsInNaturalOrderR() {
        Route route = makeRoute(R);
        int i = 4;
        for (RouteStop stop : route.getRoutePointsInNaturalOrder()) {
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
        List<RouteStop> stops = new LinkedList<>();
        for (int i = 1; i <= length ; i++) {
            RouteStop stop = new RouteStop();
            stop.setSequence(i);
            stops.add(stop);
        }
        Route route = new Route();
        route.setDirection(direction);
        route.setStops(stops);
        return route;
    }

    @Test
    void isDistanceFulfilled() {
        Route route = new Route();
        for (int i = 0; i < 4; i++) {
            route.getStops().add(newStopWithDistance());
        }
        assertThat(route.isDistanceFulfilled(), is(true));
    }

    @Test
    void isDistanceFulfilledNegativeFull() {
        Route route = new Route();
        for (int i = 0; i < 4; i++) {
            route.getStops().add(new RouteStop());
        }
        assertThat(route.isDistanceFulfilled(), is(false));
    }

    @Test
    void isDistanceFulfilledNegative() {
        Route route = new Route();
        route.getStops().add(newStopWithDistance());
        route.getStops().add(newStopWithDistance());
        route.getStops().add(new RouteStop());
        route.getStops().add(newStopWithDistance());
        assertThat(route.isDistanceFulfilled(), is(false));
    }

    private RouteStop newStopWithDistance() {
        RouteStop stop = new RouteStop();
        stop.setDistance(4);
        return stop;
    }
}