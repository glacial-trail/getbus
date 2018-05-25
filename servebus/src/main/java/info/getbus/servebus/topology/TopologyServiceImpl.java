package info.getbus.servebus.topology;

import info.getbus.servebus.topology.persistence.StopPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopologyServiceImpl implements TopologyService {
    final StopPlaceRepository repo;

    @Override
    public StopPlace ensureSaved(StopPlace stop) {
        return repo.ensureSaved(stop);
    }
}