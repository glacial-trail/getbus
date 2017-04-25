package info.getbus.servebus.repository;

import info.getbus.servebus.persistence.datamappers.common.CountryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@org.springframework.stereotype.Repository
public class CountriesRepositoryImpl implements CountriesRepository {
    @Autowired
    private CountryMapper mapper;

    @Override
    @Cacheable("countries")
    public List<String> getAll() {
        return mapper.selectAllCodes();
    }
}
