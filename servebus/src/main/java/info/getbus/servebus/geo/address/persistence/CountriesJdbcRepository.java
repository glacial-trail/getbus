package info.getbus.servebus.geo.address.persistence;

import info.getbus.servebus.geo.address.persistence.mappers.CountryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountriesJdbcRepository implements CountriesRepository {
    @Autowired
    private CountryMapper mapper;

    @Override
    @Cacheable("countries")
    public List<String> getCodes() {
        return mapper.selectAllCodes();
    }
}
