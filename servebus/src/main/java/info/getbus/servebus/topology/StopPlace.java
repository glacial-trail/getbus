package info.getbus.servebus.topology;

import info.getbus.servebus.route.model.WayPoint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StopPlace {
    Long id;
    String name;
//    long address;
    Long address/*Id*/;

    public StopPlace(WayPoint stop) {
        this.name = stop.getName();
        this.address = stop.getAddress().getId();
    }
}
