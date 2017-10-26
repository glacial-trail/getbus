package info.getbus.servebus.model.route;

import lombok.Getter;
import lombok.Setter;

import java.util.Deque;
import java.util.LinkedList;

import static info.getbus.servebus.model.route.Direction.F;

@Getter
@Setter
public class Route {
    private Long id;
    private String name;
    private Direction direction = F;
    private Deque<RoutePoint> routePoints = new LinkedList<>();

    public boolean isForward() {
        return F == direction;
    }
}
