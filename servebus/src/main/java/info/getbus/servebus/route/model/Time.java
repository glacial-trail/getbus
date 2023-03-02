package info.getbus.servebus.route.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class Time {
    private long stopId;
    private LocalTime arrival;
    private LocalTime departure;
    //    private Duration tripTime;
    private int tripTime;
}
