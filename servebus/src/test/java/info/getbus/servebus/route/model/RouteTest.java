package info.getbus.servebus.route.model;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static info.getbus.servebus.route.model.Direction.F;
import static info.getbus.servebus.route.model.Direction.R;
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
        for (WayPoint wp : route.getRoutePointsInNaturalOrder()) {
            assertThat(wp.getId(), is(i++));
        }
    }

    @Test
    void getRoutePointsInNaturalOrderR() {
        Route route = makeRoute(R);
        long i = 4;
        for (WayPoint wp : route.getRoutePointsInNaturalOrder()) {
            assertThat(wp.getId(), is(i--));
        }
    }

    private Route makeRoute(Direction direction) {
        Deque<WayPoint> wps = new LinkedList<>();
        for (long i = 1; i <= 4 ; i++) {
            WayPoint wp = new WayPoint();
            wp.setId(i);
            wps.add(wp);
        }

        Route route = new Route();
        route.setDirection(direction);
        route.setWayPoints(wps);
        return route;
    }

    @Test
    void isDistanceFulfilled() {
        Route route = new Route();
        for (int i = 0; i < 4; i++) {
            route.getWayPoints().add(newWPWithDistance());
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
        route.getWayPoints().add(newWPWithDistance());
        route.getWayPoints().add(newWPWithDistance());
        route.getWayPoints().add(new WayPoint());
        route.getWayPoints().add(newWPWithDistance());
        assertThat(route.isDistanceFulfilled(), is(false));
    }

    private WayPoint newWPWithDistance() {
        WayPoint wp = new WayPoint();
        wp.setDistance(4);
        return wp;
    }
}