package info.getbus.servebus.border.modelmapping;

import info.getbus.servebus.model.user.Profile;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RouteStop;
import info.getbus.servebus.web.dto.route.RouteDTO;
import info.getbus.servebus.web.dto.route.StopDTO;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.mockito.Mockito.when;

class RouteConversionTest extends ConversionBaseTest {

    private void setUpUsersTimezone() {
        setUpSecurityHelper();
        Profile p = new Profile();
        p.setTimeZone(ZoneId.of("GMT"));
        when(user.getProfile()).thenReturn(p);
    }

    @Test
    void dtoToRoute() {
        setUpUsersTimezone();
        validateMapping(RouteDTO.class, Route.class);
    }

    @Test
    void routeToDto() {
        validateMapping(Route.class, RouteDTO.class);
    }

    @Test
    void dtoToStop() {
        validateMapping(StopDTO.class, RouteStop.class);
    }

    @Test
    void stopToDto() {
        validateMapping(RouteStop.class, StopDTO.class);
    }
}