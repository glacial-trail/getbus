package info.getbus.servebus.route.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RoundRouteSummary {
    private long id;
    private String name;
    private List<CompactRoute> routes;
    private String lockOwner;
}
