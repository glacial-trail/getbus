package info.getbus.servebus.border.modelmapping;

import com.github.rozidan.springboot.modelmapper.TypeMapConfigurer;
import info.getbus.servebus.store.Store;
import info.getbus.servebus.web.dto.store.StoreDetailsDTO;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class StoreToDto extends TypeMapConfigurer<Store, StoreDetailsDTO> {

    @Override
    public void configure(TypeMap<Store, StoreDetailsDTO> typeMap) {
        typeMap
                .addMappings(mapping -> mapping.skip(StoreDetailsDTO::setDummyp));
    }
}