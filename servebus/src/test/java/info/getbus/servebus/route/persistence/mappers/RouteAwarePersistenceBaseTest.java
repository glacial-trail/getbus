package info.getbus.servebus.route.persistence.mappers;

import info.getbus.servebus.dao.security.UserMapper;
import info.getbus.servebus.geo.address.Address;
import info.getbus.servebus.geo.address.persistence.mappers.AddressMapper;
import info.getbus.servebus.persistence.datamappers.transporter.TransporterAreaMapper;
import info.getbus.servebus.persistence.datamappers.transporter.TransporterMapper;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.RouteStop;
import info.getbus.servebus.route.persistence.RouteAwareBaseTest;
import info.getbus.servebus.service.transporter.TransporterService;
import info.getbus.servebus.service.transporter.TransporterServiceImpl;
import info.getbus.servebus.topology.StopPlace;
import info.getbus.servebus.topology.persistence.mappers.StopPlaceMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static info.getbus.test.util.Assertions.assertThatObjectsAreEqualUsingFields;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {RouteAwarePersistenceBaseTest.class})
@Configuration
@ImportResource("classpath:persistence-config.xml")
@Transactional
@Rollback
@Ignore
public class RouteAwarePersistenceBaseTest extends RouteAwareBaseTest {
    @Bean
    public TransporterService transporterService(
            @Autowired TransporterAreaMapper transporterAreaMapper,
            @Autowired TransporterMapper transporterMapper
    ) {
        return new TransporterServiceImpl(transporterAreaMapper, transporterMapper);
    }

    @Autowired
    protected TransporterService transporterService;
    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected AddressMapper addressMapper;
    @Autowired
    protected StopPlaceMapper stopPlaceMapper;
    @Autowired
    protected RouteMapper routeMapper;
    @Autowired
    protected RouteStopMapper routeStopMapper;

    protected long transporterAreaId;

    StopPlace newPlace(Address address) {
        StopPlace place = new StopPlace();
        place.setName("name"+address.getId());
        place.setAddress(address.getId());
        return place;
    }

    protected Route newRouteWithPersistedTopology() {
        Route route = newRoute();
        //workaround. seems to be random beans do not uses setter
        route.setStops(route.getStops());
        persistTopology(route);
        return route;
    }

    private void persistTopology(Route route) {
        for (RouteStop stop : route.getStops()) {
            Address address = stop.getAddress();
            addressMapper.insert(address);
            addressMapper.insertL10n(address);
            StopPlace stopPlace = newPlace(address);
            stopPlaceMapper.insert(stopPlace);
            stop.setStopId(stopPlace.getId());
        }
    }

    @Before
    public final void setUpUserAndTransporterArea() {
        transporterAreaId = transporterService.createInitialTransporterInfrastructure();
        userMapper.insert(user);
        transporterService.linkUserToArea(transporterAreaId, user, "ROLE");
    }

    protected void insertStopsFor(Route route) {
        insertStopsFor(route, false);
    }
    protected void insertStopsFor(Route route, boolean insertStopsData) {
        for (RouteStop stop : route.getRoutePointsInNaturalOrder()) {
            routeStopMapper.upsert(stop);
            if (insertStopsData) {
                routeStopMapper.upsertLength(stop, route.getDirection());
                routeStopMapper.upsertTimetable(stop, route.getDirection());
            }
        }
    }

    protected void assertThatStopsAreEqual(RouteStop actual, RouteStop expected) {
        assertThatObjectsAreEqualUsingFields(actual, expected, "routeId", "stopId", "sequence", "name");
    }

    protected void assertThatStopsDataAreEqual(RouteStop actual, RouteStop expected) {
        assertThatObjectsAreEqualUsingFields(actual, expected, "arrival", "departure", "distance", "tripTime");
    }
}