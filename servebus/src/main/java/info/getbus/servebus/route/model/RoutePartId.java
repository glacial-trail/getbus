package info.getbus.servebus.route.model;

import static info.getbus.servebus.route.model.Direction.F;
import static info.getbus.servebus.route.model.Direction.R;

public class RoutePartId {
    private Long id;
    private Direction direction;

    public RoutePartId() { }

    public RoutePartId(Long id, Direction direction) {
        this.id = id;
        this.direction = direction;
    }

    public static RoutePartId forward(Long id) {
        return new RoutePartId(id, F);
    }

    public static RoutePartId reverse(Long id) {
        return new RoutePartId(id, R);
    }

    public Long getId() {
        return id;
    }

    public Direction getDirection() {
        return direction;
    }
}
