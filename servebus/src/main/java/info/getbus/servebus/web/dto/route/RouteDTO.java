package info.getbus.servebus.web.dto.route;

import info.getbus.servebus.route.model.Direction;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static info.getbus.servebus.route.model.Direction.F;

@Getter
@Setter
public class RouteDTO {
//    private Long roundRouteId;
//    @NotEmpty(message = "common.notempty.error")
//    private String roundRouteName;
    @Valid
    private RoundRouteSummaryDTO roundRouteSummary;

    private Long id;

    @NotEmpty(message = "common.notempty.error")
    private String name;
    @NotNull(message = "common.notempty.error")
    @NumberFormat(pattern = "#####0.00")
    private BigDecimal basePrice;
    @NotNull(message = "common.notempty.error")
    private int baseSeatsQty;
    @NotNull(message = "common.notempty.error")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startSales;
//    private ZonedDateTime startSales;
    @NotNull(message = "common.notempty.error")
    private int salesDepth;
    @NotNull(message = "common.notempty.error")
    private Direction direction = F;
    @Valid
    @NotEmpty(message = "common.notempty.error")
    @Size(min = 2)
    private List<StopDTO> stops = new LinkedList<>();
}
