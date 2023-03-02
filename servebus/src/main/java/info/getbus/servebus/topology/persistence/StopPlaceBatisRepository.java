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
/*
    public StopPlace ensureSaved2(StopPlace stop) {
        if (stop.getId() == null) {
            StopPlace saved = mapper.selectByAddress(stop.getAddress());
            if (null == saved) {
                mapper.insert(stop);
                saved = stop;
            }
            return saved;
        }

        StopPlace saved = mapper.selectById(stop.getId());
        if (saved == null) {
            stop.setId(null);
            saved = mapper.selectByAddress(stop.getAddress());
            if (null == saved) {
                mapper.insert(stop);
                saved = stop;
            }
            return saved;
        }

        if (saved.getAddress().equals(stop.getAddress())) {
            return saved;
        } else {
            stop.setId(null);
            mapper.insert(stop);
            return stop;
        }
    }
*/
}
