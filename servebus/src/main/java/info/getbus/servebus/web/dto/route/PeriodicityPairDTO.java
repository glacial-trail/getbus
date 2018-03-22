package info.getbus.servebus.web.dto.route;


import info.getbus.servebus.route.model.PeriodicityStrategy;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PeriodicityPairDTO {
    @NotNull
    @Min(1)
    private Long routeId;
    @NotNull
    private PeriodicityStrategy strategy;
    @NotNull
    @Valid
    private PeriodicityPartDTO forward;
    @NotNull
    @Valid
    private PeriodicityPartDTO reverse;

    public static PeriodicityPairDTO empty(Long rid) {
        PeriodicityPairDTO dto = new PeriodicityPairDTO();
        dto.setRouteId(rid);
        dto.setForward(new PeriodicityPartDTO());
        dto.setReverse(new PeriodicityPartDTO());
        return dto;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public PeriodicityStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(PeriodicityStrategy strategy) {
        this.strategy = strategy;
    }

    public PeriodicityPartDTO getForward() {
        return forward;
    }

    public void setForward(PeriodicityPartDTO forward) {
        this.forward = forward;
    }

    public PeriodicityPartDTO getReverse() {
        return reverse;
    }

    public void setReverse(PeriodicityPartDTO reverse) {
        this.reverse = reverse;
    }
}
