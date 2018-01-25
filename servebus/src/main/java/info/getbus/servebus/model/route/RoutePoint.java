package info.getbus.servebus.model.route;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

//TODO GREAT refactoring: lombok, modelmapper etc
//TODO rename to WayPoint
public class RoutePoint {
    private Long id;
    @NotEmpty(message = "common.notempty.error")
    private String name;
    @NotEmpty(message = "common.notempty.error")
    private String countryCode;
    @NotEmpty(message = "common.notempty.error")
    private String address;
//    @DateTimeFormat(iso = ISO.TIME)
    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime arrival;
    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime departure;
//    private Duration tripTime;
    @NotNull
    private Long tripTime;
    @NotNull
    private Integer distance;

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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalTime arrival) {
        this.arrival = arrival;
    }

    public LocalTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalTime departure) {
        this.departure = departure;
    }

    public Long getTripTime() {
        return tripTime;
    }

    public void setTripTime(Long tripTime) {
        this.tripTime = tripTime;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
