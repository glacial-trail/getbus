package info.getbus.servebus.persistence.datamappers.user;

import org.apache.ibatis.annotations.Param;

public interface ProfileMapper {
    void insert(@Param("username") String username, @Param("timeZone") String timeZone);
}
