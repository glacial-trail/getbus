package info.getbus.servebus.topology.persistence;


import info.getbus.servebus.topology.StopPlace;

public interface StopPlaceRepository {
    StopPlace ensureSaved(StopPlace stopPlace);
}
