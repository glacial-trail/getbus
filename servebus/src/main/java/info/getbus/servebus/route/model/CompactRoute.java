package info.getbus.servebus.route.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompactRoute {
    private long id;
    private String name;
    private String startStop;
    private String endStop;
    private boolean editable;//TODO lockOwner

    public CompactRoute(Route route) {
        id = route.getId();
        name = route.getName();
        if (!route.getStops().isEmpty()) {
            startStop = route.getFirstStop().getName();
            endStop = route.getLastStop().getName();
        }
    }
}
