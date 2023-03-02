package info.getbus.servebus.web.dto.route;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Getter @Setter
public class RoundRouteSummaryDTO {
    private long id;
    @NotEmpty(message = "common.notempty.error")
    private String name;
    private List<RouteSummaryDTO> routes;
    private boolean editable;
}
