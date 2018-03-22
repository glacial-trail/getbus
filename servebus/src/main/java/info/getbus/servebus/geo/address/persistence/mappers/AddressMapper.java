package info.getbus.servebus.geo.address.persistence.mappers;

import info.getbus.servebus.geo.address.Address;
import org.apache.ibatis.annotations.Param;

public interface AddressMapper {
    Address select(@Param("id") long id);
    Address selectSame(@Param("address") Address address);
    void insert(@Param("address") Address address);
    void insertL10n(@Param("address") Address address);
}
