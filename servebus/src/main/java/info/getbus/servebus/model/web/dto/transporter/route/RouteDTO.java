package info.getbus.servebus.model.web.dto.transporter.route;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

public class RouteDTO {
    private Long id;

    @NotEmpty(message = "common.notempty.error")
    private String name;

    @NotNull(message = "common.notempty.error")
    private Direction direction = Direction.F;

    @Valid
    @NotEmpty(message = "common.notempty.error")
    private List<RoutePointDTO> routePoints = new LinkedList<>();

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

    public List<RoutePointDTO> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<RoutePointDTO> routePoints) {
        this.routePoints = routePoints;
    }
}
