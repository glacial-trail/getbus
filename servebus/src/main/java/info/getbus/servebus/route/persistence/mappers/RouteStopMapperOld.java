package info.getbus.servebus.route.persistence.mappers;

import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.RouteStop;
import org.apache.ibatis.annotations.Param;

@Deprecated
public interface RouteStopMapperOld {
    /*will be*/@Deprecated
    boolean existsStop(@Param("stop") RouteStop stop);
    /*will be*/@Deprecated
    void insertStop(@Param("stop") RouteStop stop);
    /*will be*/@Deprecated
    void updateStop(@Param("stop") RouteStop stop);
    /*will be*/@Deprecated
    boolean existsLength(@Param("stop") RouteStop stop, @Param("direction") Direction direction);
    /*will be*/@Deprecated
    void insertLength(@Param("stop") RouteStop stop, @Param("direction") Direction direction);
    /*will be*/@Deprecated
    void updateLength(@Param("stop") RouteStop stop, @Param("direction") Direction direction);
    /*will be*/@Deprecated
    boolean existsTimetable(@Param("stop") RouteStop stop, @Param("direction") Direction direction);
    /*will be*/@Deprecated
    void insertTimetable(@Param("stop") RouteStop stop, @Param("direction") Direction direction);
    /*will be*/@Deprecated
    void updateTimetable(@Param("stop") RouteStop stop, @Param("direction") Direction direction);
}
