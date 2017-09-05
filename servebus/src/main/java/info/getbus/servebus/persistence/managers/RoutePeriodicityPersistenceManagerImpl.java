package info.getbus.servebus.persistence.managers;

import info.getbus.servebus.model.periodicity.Periodicity;
import info.getbus.servebus.model.route.*;
import info.getbus.servebus.persistence.InconsistentModelDataException;
import info.getbus.servebus.persistence.datamappers.route.RoutePeriodicityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

@Service
public class RoutePeriodicityPersistenceManagerImpl implements RoutePeriodicityPersistenceManager {
    @Autowired
    private RoutePeriodicityMapper routePeriodicityMapper;


    @Nullable
    @Override
    public PeriodicityPair getPair(long routeId) {
        List<RoutePeriodicity> periodicities = routePeriodicityMapper.selectByRouteId(routeId);
        if (periodicities.isEmpty()) {
            return null;
        }
        if (periodicities.size() != 2) {
            throw new InconsistentModelDataException(PeriodicityPair.class, "More than 2 periodicity records in database");
        }

        RoutePeriodicity periodicity = periodicities.get(0);
        RoutePeriodicity oppositePeriodicity;
        if (periodicity.getRoutePartId().getDirection() == Direction.F) {
            oppositePeriodicity = periodicities.get(1);
        } else {
            oppositePeriodicity = periodicity;
            periodicity = periodicities.get(1);
        }
        return new PeriodicityPair(routeId, periodicity, oppositePeriodicity);
    }

    @Override
    public void save(RoutePartId routeId, Periodicity periodicity) {
        if (null == periodicity.getId()) {
            routePeriodicityMapper.insert(routeId, periodicity);
        } else {
            routePeriodicityMapper.update(routeId.getId(), periodicity);
        }
    }
}
