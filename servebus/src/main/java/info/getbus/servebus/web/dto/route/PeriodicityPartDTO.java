package info.getbus.servebus.web.dto.route;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

//TODO make complex interval&weekDays validation
public class PeriodicityPartDTO {

    private Long id;
//    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate start;
    //TODO time zone validation
//    @Min(0)
    private Integer interval;
    private boolean[] weekDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public boolean[] getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(boolean[] weekDays) {
        this.weekDays = weekDays;
    }
}
