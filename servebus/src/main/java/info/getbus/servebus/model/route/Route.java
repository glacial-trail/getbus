package info.getbus.servebus.model.route;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

import static info.getbus.servebus.model.route.Direction.F;

public class Route {
    private Long id;

    @NotEmpty(message = "common.notempty.error")
    private String name;

    @NotNull(message = "common.notempty.error")
    private Direction direction = F;

    @Valid
    @NotEmpty(message = "common.notempty.error")
    private List<RoutePoint> routePoints = new LinkedList<>();

    public boolean isForward() {
        return F == direction;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public List<RoutePoint> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<RoutePoint> routePoints) {
        this.routePoints = routePoints;
    }
}
