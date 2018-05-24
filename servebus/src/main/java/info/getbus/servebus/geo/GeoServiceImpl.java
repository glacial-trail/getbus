package info.getbus.servebus.geo;

import info.getbus.servebus.geo.address.Address;
import info.getbus.servebus.geo.address.persistence.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoServiceImpl implements GeoService {
    private final AddressRepository repo;

    @Override
    public Address ensureSaved(Address address) {
        return repo.ensureSaved(address);
    }
}
