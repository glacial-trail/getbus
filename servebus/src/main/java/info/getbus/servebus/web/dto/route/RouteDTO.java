package info.getbus.servebus.web.dto.route;

import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.WayPoint;
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
import java.util.Deque;
import java.util.LinkedList;

import static info.getbus.servebus.route.model.Direction.F;

@Getter
@Setter
public class RouteDTO {
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
    private Deque<WayPoint> wayPoints = new LinkedList<>();
}
