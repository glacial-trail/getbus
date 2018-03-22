package info.getbus.servebus.geo.address;

import info.getbus.servebus.common.model.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address implements Entity {
    private Long id;
//    private Lang lang;
    private String countryCode;
    private String adminArea1;
    private String city;
    private String streetBuilding;
    private String street;
    private String building;
    private String zip;
    private int utcOffset;
}
