package info.getbus.servebus.topology.persistence;

import info.getbus.servebus.topology.StopPlace;
import info.getbus.servebus.topology.persistence.mappers.StopPlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StopPlaceBatisRepository implements StopPlaceRepository {
    private final StopPlaceMapper mapper;

    @Override
    public StopPlace ensureSaved(StopPlace stop) {
        StopPlace saved = mapper.selectByAddress(stop.getAddress());
        if (null == saved) {
            mapper.insert(stop);
            saved = stop;
        }
        return saved;
    }
}
