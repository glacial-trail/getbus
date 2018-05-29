package info.getbus.servebus.model.user;

import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;

@Getter @Setter
public class Profile {
    private ZoneId timeZone;
}
