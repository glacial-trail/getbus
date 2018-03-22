package info.getbus.servebus.route.model;

import java.time.ZonedDateTime;

public class Periodicity {
    private Long id;
    private ZonedDateTime start;
    private PeriodicityStrategy strategy;
    private int param;

    public Periodicity() { }

    public Periodicity(Long id, ZonedDateTime start, PeriodicityStrategy strategy, int param) {
        this(start, strategy, param);
        this.id = id;
    }
    public Periodicity(ZonedDateTime start, PeriodicityStrategy strategy, int param) {
        this.start = start;
        this.strategy = strategy;
        this.param = param;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public PeriodicityStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(PeriodicityStrategy strategy) {
        this.strategy = strategy;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }
}
