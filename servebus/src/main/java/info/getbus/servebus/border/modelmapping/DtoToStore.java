package info.getbus.servebus.border.modelmapping;

import com.github.rozidan.springboot.modelmapper.TypeMapConfigurer;
import info.getbus.servebus.service.security.SecurityHelper;
import info.getbus.servebus.store.Store;
import info.getbus.servebus.web.dto.store.StoreDetailsDTO;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DtoToStore extends TypeMapConfigurer<StoreDetailsDTO, Store> {

    @Autowired
    private SecurityHelper securityHelper;


    //autowire carier helper,

    @Override
    public void configure(TypeMap<StoreDetailsDTO, Store> typeMap) {

//        Long carrierId = securityHelper.getCurrentUser().getTransporterAreaId();

        Converter<String, Long> converter = context -> securityHelper.getCurrentUser().getTransporterAreaId();


        typeMap

                //ger carrrier id, and set it
                .addMappings(mapping -> mapping
                        .using(converter)
                        .map(StoreDetailsDTO::getDummyp, Store::setCarrierId)

                )

                .addMappings(mapping -> mapping.map(StoreDetailsDTO::getId, Store::setId))
                .addMappings(mapping -> mapping.map(StoreDetailsDTO::getDomain, Store::setDomain))
                .addMappings(mapping -> mapping
                        .<String>map(StoreDetailsDTO::getPaymentDetailsAccount, (dst, v) -> dst.getPaymentDetails().setAccount(v)))
                .addMappings(mapping -> mapping
                        .<String>map(StoreDetailsDTO::getPaymentDetailsMfo, (dst, v) -> dst.getPaymentDetails().setMfo(v)))
                .addMappings(mapping -> mapping
                        .<String>map(StoreDetailsDTO::getPaymentDetailsOkpo, (dst, v) -> dst.getPaymentDetails().setOkpo(v)))
                .addMappings(mapping -> mapping
                        .<String>map(StoreDetailsDTO::getPaymentDetailsPhone, (dst, v) -> dst.getPaymentDetails().setPhone(v)))
//                .addMappings(mapping -> mapping.map(storeDetailsDTO))
//                .addMappings(mapping -> mapping.map(storeDetailsDTO))
//                .addMappings(mapping -> mapping.skip(Store::setCarrierId))
                .addMappings(mapping -> mapping.skip(Store::setPaymentDetails))
                .addMappings(mapping -> mapping.<Long>skip((dst, v) -> dst.getPaymentDetails().setId(v)))
                .addMappings(mapping -> mapping.<Store>skip((dst, v) -> dst.getPaymentDetails().setStore(v)))
                .addMappings(mapping -> mapping.<Store>skip((dst, v) -> dst.getPaymentDetails().setStore(v)))
//                .addMappings(mapping -> mapping.skip(mapping.))
        ;
    }

    @Override
    public Configuration getConfiguration() {
        InheritingConfiguration c = new InheritingConfiguration();
        c.setImplicitMappingEnabled(false);
        return c;
    }
}