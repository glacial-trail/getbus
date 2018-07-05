package info.getbus.servebus.border.modelmapping;

import com.github.rozidan.springboot.modelmapper.TypeMapConfigurer;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.service.security.SecurityHelper;
import info.getbus.servebus.web.dto.route.RouteDTO;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;

@Component
public class DtoToRoute extends TypeMapConfigurer<RouteDTO, Route> {
    private SecurityHelper securityHelper;

    public DtoToRoute(@Autowired SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }

    @Override
    public void configure(TypeMap<RouteDTO, Route> typeMap) {
        Converter<LocalDate, ZonedDateTime> converter = context
                -> ZonedDateTime.of(context.getSource(), LocalTime.MIDNIGHT, resolveCurrentUserTimezone());
        typeMap
                .addMappings(mapping -> mapping
                        .using(converter)
                        .map(RouteDTO::getStartSales, Route::setStartSales))
                .addMappings(mapping -> mapping
                        .with(request -> new LinkedList<>())
                        .map(RouteDTO::getStops, Route::setWayPoints));
    }

    private ZoneId resolveCurrentUserTimezone() {
        return securityHelper.getCurrentUser().getProfile().getTimeZone();
    }
}
