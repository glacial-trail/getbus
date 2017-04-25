package info.getbus.servebus.persistence.datamappers.common;

import java.util.List;

public interface CountryMapper {
    List<String> selectAllCodes();
}