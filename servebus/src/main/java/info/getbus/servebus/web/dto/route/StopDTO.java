package info.getbus.servebus.web.dto.route;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@Setter
public class StopDTO {
    @NotEmpty(message = "common.notempty.error")
    private String name;
/*  TODO
    @Valid
    @NotNull
    private AddressDTO address;
*/
    private Long addressId;
//  private Lang addressLang; TODO
    @NotEmpty(message = "common.notempty.error")
    private String addressCountryCode;
//    @NotEmpty(message = "common.notempty.error")
    private String addressAdminArea1;
    @NotEmpty(message = "common.notempty.error")
    private String addressCity;
    @NotEmpty(message = "common.notempty.error")
    private String addressStreet;
//    @NotEmpty(message = "common.notempty.error")
    private String addressStreetName;
//  @NotEmpty ?
    private String addressBuildingNumber;
//  @NotEmpty ?
    private String addressZip;
    @NotNull(message = "common.notempty.error")
    private Integer addressUtcOffset;
    @NotNull(message = "common.notempty.error")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime arrival;
    @NotNull(message = "common.notempty.error")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime departure;
    @NotNull(message = "common.notempty.error")
    private /*TODO Duration*/ Integer tripTime;
    @NotNull(message = "common.notempty.error")
    private Integer distance;
//    @NotNull(message = "common.notempty.error")
    private Double lat;
//    @NotNull(message = "common.notempty.error")
    private Double lon;
}
