package info.getbus.servebus.persistence.datamappers.transporter;

import org.apache.ibatis.annotations.Param;

public interface TransporterMapper {
    void insertEmpty(@Param("id") Long id);
}
