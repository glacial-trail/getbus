package info.getbus.servebus.geo;

import info.getbus.servebus.geo.address.Address;

public interface GeoService {
    Address ensureSaved(Address address);
}
