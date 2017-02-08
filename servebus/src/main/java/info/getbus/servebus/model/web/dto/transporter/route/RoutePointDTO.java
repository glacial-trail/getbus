package info.getbus.servebus.model.web.dto.transporter.route;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

public class RoutePointDTO {
    private Long id;
    private String name;
    private String address;
//    @DateTimeFormat(iso = ISO.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime arrival;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime departure;
//    private Duration tripTime;
    private Long tripTime;
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
