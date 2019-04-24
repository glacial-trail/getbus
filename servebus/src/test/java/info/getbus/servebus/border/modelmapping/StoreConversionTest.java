package info.getbus.servebus.border.modelmapping;

import info.getbus.servebus.store.Store;
import info.getbus.servebus.web.dto.store.StoreDetailsDTO;
import org.junit.jupiter.api.Test;

class StoreConversionTest extends ConversionBaseTest {

    @Test
    void dtoToRoute() {
        validateMapping(Store.class, StoreDetailsDTO.class);
    }

    @Test
    void routeToDto() {
        setUpSecurityHelper();
        validateMapping(StoreDetailsDTO.class, Store.class);
    }
}