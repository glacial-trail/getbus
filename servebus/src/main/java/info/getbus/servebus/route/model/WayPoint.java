package info.getbus.servebus.route.model;

import info.getbus.servebus.geo.address.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter @Setter
public class WayPoint {
    private Long routeId;
    private Long stopId;
    private int sequence;
    private String name;
//  private long addressId;
    private Address address;
    private /*int*/Integer distance;
    private LocalTime arrival;
    private LocalTime departure;
    private /*TODO typeHandler Duration */Integer tripTime;
}
