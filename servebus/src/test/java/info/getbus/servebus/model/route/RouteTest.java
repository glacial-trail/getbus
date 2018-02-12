package info.getbus.servebus.model.route;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static info.getbus.servebus.model.route.Direction.F;
import static info.getbus.servebus.model.route.Direction.R;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class RouteTest {

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
    void getRoutePointsInNaturalOrderF() {
        Route route = makeRoute(F);
        long i = 1;
        for (RoutePoint wp : route.getRoutePointsInNaturalOrder()) {
            assertThat(wp.getId(), is(i++));
        }
    }

    @Test
    void getRoutePointsInNaturalOrderR() {
        Route route = makeRoute(R);
        long i = 4;
        for (RoutePoint wp : route.getRoutePointsInNaturalOrder()) {
            assertThat(wp.getId(), is(i--));
        }
    }

    private Route makeRoute(Direction direction) {
        Deque<RoutePoint> wps = new LinkedList<>();
        for (long i = 1; i <= 4 ; i++) {
            RoutePoint wp = new RoutePoint();
            wp.setId(i);
            wps.add(wp);
        }

        Route route = new Route();
        route.setDirection(direction);
        route.setRoutePoints(wps);
        return route;
    }
}