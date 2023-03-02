package info.getbus.servebus.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class OneOfRoundRoute {
    private Long roundRouteId;
    private final Route route;
}
