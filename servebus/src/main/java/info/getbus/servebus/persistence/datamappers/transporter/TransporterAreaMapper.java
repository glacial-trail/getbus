package info.getbus.servebus.persistence.datamappers.transporter;

import info.getbus.servebus.model.transporter.TransporterArea;
import org.apache.ibatis.annotations.Param;

public interface TransporterAreaMapper {
    void insertEmpty(TransporterArea transporterArea);
    void insertUser2Transporter(@Param("transporterId") Long areaId, @Param("username") String username, @Param("authority") String authority);
}
