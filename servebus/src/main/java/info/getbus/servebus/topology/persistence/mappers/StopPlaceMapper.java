package info.getbus.servebus.topology.persistence.mappers;

import info.getbus.servebus.topology.StopPlace;

public interface StopPlaceMapper {
    StopPlace selectByAddress(long addressId);
    void insert(StopPlace stop);
}
