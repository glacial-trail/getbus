package info.getbus.servebus.web.dto.geo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddressDTO {
    private Long id;
//    private Lang lang;
    @NotEmpty
    private String countryCode;
    @NotEmpty
    private String adminArea1;
    @NotEmpty
    private String city;
    @NotEmpty
    private String streetBuilding;
    @NotEmpty
    private String street;
    //    @NotEmpty
    private String building;
//    @NotEmpty
    private String zip;
    @NotNull
    private Integer utcOffset;
}
