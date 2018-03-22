package info.getbus.servebus.geo.address.persistence.mappers;

import java.util.List;

public interface CountryMapper {
    List<String> selectAllCodes();
}