package info.getbus.servebus.model.route;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Deque;
import java.util.LinkedList;

import static info.getbus.servebus.model.route.Direction.F;
import static info.getbus.servebus.model.route.Direction.R;

@Getter
@Setter
public class Route {
    private Long id;
    private String name;
    private BigDecimal basePrice;
    private int baseSeatsQty;
    private ZonedDateTime startSales;
    private int salesDepth;
    private Direction direction = F;
    private Deque<RoutePoint> routePoints = new LinkedList<>();

    public boolean isForward() {
        return F == direction;
    }

    public Direction oppositeDirection() {
        return isForward() ? R : F;
    }

    /**
     * @return new copy of way points collection in forward direction
     */
    public Deque<RoutePoint> getRoutePointsInNaturalOrder() {
        if (isForward()) {
            return new LinkedList<>(routePoints);
        } else {
            return reverse(routePoints);
        }
    }

    private Deque<RoutePoint> reverse(Deque<RoutePoint> routePoints) {
        Deque<RoutePoint> reversed = new LinkedList<>();
        routePoints.forEach(reversed::addFirst);
        return reversed;
    }

    public boolean isDistanceFulfilled() {
        return routePoints.stream().noneMatch(wp -> null == wp.getDistance());
    }
}
