package info.getbus.servebus.border.modelmapping;

import com.github.rozidan.springboot.modelmapper.TypeMapConfigurer;
import info.getbus.servebus.route.model.RouteStop;
import info.getbus.servebus.web.dto.route.StopDTO;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class DtoToStop extends TypeMapConfigurer<StopDTO, RouteStop> {

    @Override
    public void configure(TypeMap<StopDTO, RouteStop> typeMap) {
        typeMap
                .addMappings(mapping -> mapping
                        .<String>map(StopDTO::getAddressStreet, (dst, val) -> dst.getAddress().setStreetBuilding(val)))
                .addMappings(mapping -> mapping
                        .<String>map(StopDTO::getAddressStreetName, (dst, val) -> dst.getAddress().setStreet(val)))
                .addMappings(mapping -> mapping
                        .<String>map(StopDTO::getAddressBuildingNumber, (dst, val) -> dst.getAddress().setBuilding(val)))
                .addMappings(mapping -> mapping.skip(RouteStop::setRouteId))
                .addMappings(mapping -> mapping.skip(RouteStop::setStopId))
                .addMappings(mapping -> mapping.skip(RouteStop::setSequence));
    }
}