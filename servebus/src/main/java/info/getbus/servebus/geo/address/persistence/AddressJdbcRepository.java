package info.getbus.servebus.geo.address.persistence;

import info.getbus.servebus.geo.address.Address;
import info.getbus.servebus.geo.address.persistence.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AddressJdbcRepository implements AddressRepository {
    private AddressMapper mapper;

    public AddressJdbcRepository(@Autowired AddressMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public Address get(long id) {
        return mapper.select(id);
    }

    @Override
    public Address ensureSaved(Address address) {
        Address saved = mapper.selectSame(address);
        if (null == saved) {
            mapper.insert(address);
            mapper.insertL10n(address);
            saved = address;
        }
        return saved;
    }
}