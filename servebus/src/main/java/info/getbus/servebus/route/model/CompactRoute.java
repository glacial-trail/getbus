package info.getbus.servebus.route.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompactRoute {
    private long id;
    private String name;
    private String startStop;
    private String endStop;
    private boolean editable;
}
