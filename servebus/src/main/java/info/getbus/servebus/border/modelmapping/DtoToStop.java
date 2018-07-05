package info.getbus.servebus.border.modelmapping;

import com.github.rozidan.springboot.modelmapper.TypeMapConfigurer;
import info.getbus.servebus.route.model.WayPoint;
import info.getbus.servebus.web.dto.route.StopDTO;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class DtoToStop extends TypeMapConfigurer<StopDTO, WayPoint> {

    @Override
    public void configure(TypeMap<StopDTO, WayPoint> typeMap) {
        typeMap
                .addMappings(mapping -> mapping
                        .<String>map(StopDTO::getAddressStreet, (dst, val) -> dst.getAddress().setStreetBuilding(val)))
                .addMappings(mapping -> mapping
                        .<String>map(StopDTO::getAddressStreetName, (dst, val) -> dst.getAddress().setStreet(val)))
                .addMappings(mapping -> mapping
                        .<String>map(StopDTO::getAddressBuildingNumber, (dst, val) -> dst.getAddress().setBuilding(val)))
                .addMappings(mapping -> mapping.skip(WayPoint::setRouteId))
                .addMappings(mapping -> mapping.skip(WayPoint::setStopId))
                .addMappings(mapping -> mapping.skip(WayPoint::setSequence));
    }
}