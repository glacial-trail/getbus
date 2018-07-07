package info.getbus.servebus.border.modelmapping;

import com.github.rozidan.springboot.modelmapper.TypeMapConfigurer;
import info.getbus.servebus.route.model.RouteStop;
import info.getbus.servebus.web.dto.route.StopDTO;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class StopToDto extends TypeMapConfigurer<RouteStop, StopDTO> {

    @Override
    public void configure(TypeMap<RouteStop, StopDTO> typeMap) {
        typeMap
                .addMappings(mapping -> mapping
                        .map(src -> src.getAddress().getStreetBuilding(), StopDTO::setAddressStreet))
                .addMappings(mapping -> mapping
                        .map(src -> src.getAddress().getStreet(), StopDTO::setAddressStreetName))
                .addMappings(mapping -> mapping
                        .map(src -> src.getAddress().getBuilding(), StopDTO::setAddressBuildingNumber))
                .addMappings(mapping -> mapping.skip(StopDTO::setLat))
                .addMappings(mapping -> mapping.skip(StopDTO::setLon));
    }
}