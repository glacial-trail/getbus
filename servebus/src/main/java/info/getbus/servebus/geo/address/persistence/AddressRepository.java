package info.getbus.servebus.geo.address.persistence;

import info.getbus.servebus.geo.address.Address;

public interface AddressRepository /*extends Repository<Address>*/ {
    Address get(long id);
    Address ensureSaved(Address address);
}
