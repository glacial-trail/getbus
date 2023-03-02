package info.getbus.servebus.route.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter @Setter
public class RoundRoute {
    private long id;
    private List<Route> routes;

    public Route forward() {
        return routes.get(0);
    }

    public Optional<Route> reverse() {
        if (routes.size() > 1) {
            return Optional.of(routes.get(1));
        } else {
            return Optional.empty();
        }
    }
}
