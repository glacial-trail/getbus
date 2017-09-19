package info.getbus.servebus.model.user;

import java.time.ZoneId;

public class Profile {
    private ZoneId timeZone;

    public ZoneId getTimeZone() {
        return timeZone;
    }

    private void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }
}
