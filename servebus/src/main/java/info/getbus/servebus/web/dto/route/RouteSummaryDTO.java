package info.getbus.servebus.web.dto.route;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RouteSummaryDTO {
    private long id;
    private String name;
    private String startStop;
    private String endStop;
    private boolean editable;
}
