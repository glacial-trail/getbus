package info.getbus.servebus.route.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Deque;
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
    private List<WayPoint> wayPoints = new LinkedList<>();

    public void setId(Long id) {
        this.id = id;
        if (null != wayPoints) {
            wayPoints.forEach(stop -> stop.setRouteId(id));
        }
    }

    public void setWayPoints(List<WayPoint> wayPoints) {
        int s=1;
        List<WayPoint> stops = new LinkedList<>();
        for (WayPoint stop : wayPoints) {
            stop.setRouteId(id);
            stop.setSequence(s++);
            stops.add(stop);
        }
        this.wayPoints = stops;
    }


    public boolean isForward() {
        return F == direction;
    }

    public Direction oppositeDirection() {
        return isForward() ? R : F;
    }

    public WayPoint getFirstStop() {
        return wayPoints.iterator().next();
    }

    public WayPoint getLastStop() {
        return wayPoints.listIterator(wayPoints.size()).previous();
    }

    /**
     * @return new copy of way points collection in forward direction
     */
    public Deque<WayPoint> getRoutePointsInNaturalOrder() {
        if (isForward()) {
            return new LinkedList<>(wayPoints);
        } else {
            return reverse(wayPoints);
        }
    }

    private Deque<WayPoint> reverse(Collection<WayPoint> wayPoints) {
        Deque<WayPoint> reversed = new LinkedList<>();
        wayPoints.forEach(reversed::addFirst);
        return reversed;
    }

    public boolean isDistanceFulfilled() {
        return wayPoints.stream().noneMatch(stop -> null == stop.getDistance());
    }
}
