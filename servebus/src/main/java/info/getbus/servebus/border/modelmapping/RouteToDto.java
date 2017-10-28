package info.getbus.servebus.border.modelmapping;

import com.github.rozidan.springboot.modelmapper.TypeMapConfigurer;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.web.dto.route.RouteDTO;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.LinkedList;

@Component
public class RouteToDto extends TypeMapConfigurer<Route, RouteDTO> {

    @Override
    public void configure(TypeMap<Route, RouteDTO> typeMap) {
        Converter<ZonedDateTime, LocalDate> converter = context -> context.getSource().toLocalDate();
        typeMap
                .addMappings(mapping -> mapping
                        .using(converter)
                        .map(Route::getStartSales, RouteDTO::setStartSales))
                .addMappings(mapping -> mapping
                        .with(request -> new LinkedList<>())
                        .map(Route::getRoutePoints, RouteDTO::setRoutePoints));
    }
}
