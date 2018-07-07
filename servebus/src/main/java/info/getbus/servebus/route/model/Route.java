package info.getbus.servebus.route.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static info.getbus.servebus.route.model.Direction.F;
import static info.getbus.servebus.route.model.Direction.R;

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
    private List<RouteStop> stops = new LinkedList<>();

    public void setId(Long id) {
        this.id = id;
        if (null != stops) {
            stops.forEach(stop -> stop.setRouteId(id));
        }
    }

    public void setStops(List<RouteStop> stops) {
        int s = 1;
        List<RouteStop> updated = new LinkedList<>();
        for (RouteStop stop : stops) {
            stop.setRouteId(id);
            stop.setSequence(s++);
            updated.add(stop);
        }
        this.stops = updated;
    }


    public boolean isForward() {
        return F == direction;
    }

    public Direction oppositeDirection() {
        return isForward() ? R : F;
    }

    public RouteStop getFirstStop() {
        return stops.iterator().next();
    }

    public RouteStop getLastStop() {
        return stops.listIterator(stops.size()).previous();
    }

    /**
     * @return new copy of stops collection in forward direction
     */
    public List<RouteStop> getRoutePointsInNaturalOrder() {
        if (isForward()) {
            return new LinkedList<>(stops);
        } else {
            return reverse(stops);
        }
    }

    private List<RouteStop> reverse(Collection<RouteStop> stops) {
        LinkedList<RouteStop> reversed = new LinkedList<>();
        stops.forEach(reversed::addFirst);
        return reversed;
    }

    public boolean isDistanceFulfilled() {
        return stops.stream().noneMatch(stop -> null == stop.getDistance());
    }
}
