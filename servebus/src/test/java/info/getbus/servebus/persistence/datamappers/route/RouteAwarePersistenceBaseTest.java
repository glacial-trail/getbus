package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.dao.security.UserMapper;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.WayPoint;
import info.getbus.servebus.persistence.datamappers.transporter.TransporterAreaMapper;
import info.getbus.servebus.persistence.datamappers.transporter.TransporterMapper;
import info.getbus.servebus.service.transporter.TransporterService;
import info.getbus.servebus.service.transporter.TransporterServiceImpl;
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
    protected RouteMapper routeMapper;
    @Autowired
    protected WayPointMapper wayPointMapper;

    protected long transporterAreaId;


    @Before
    public final void setUpUserAndTransporterArea() {
        transporterAreaId = transporterService.createBaseTransporterInfrastructure();
        userMapper.insert(user);
        transporterService.linkUserToArea(transporterAreaId, user, "ROLE");
    }

    protected void insertPointsFor(Route route) {
        insertPointsFor(route, false);
    }
    protected void insertPointsFor(Route route, boolean insertPointData) {
        class InsertHelper {
            private int sequence = 1;
            private Long routeId;
            private InsertHelper(Long routeId) {
                this.routeId = routeId;
            }
            private void insertPoint(WayPoint point) {
                wayPointMapper.insert(routeId, point, sequence++);
                if (insertPointData) {
                    wayPointMapper.insertDataIfNonExist(point, route.getDirection());
                }
            }
        }
        route.getWayPoints().forEach(new InsertHelper(route.getId())::insertPoint);
    }

    protected void assertThatPointsAreEqual(WayPoint actual, WayPoint expected) {
        assertThatObjectsAreEqualUsingFields(actual, expected, "id", "name", "countryCode", "address");
    }

    protected void assertThatPointsDataAreEqual(WayPoint actual, WayPoint expected) {
        assertThatObjectsAreEqualUsingFields(actual, expected, "arrival", "departure", "distance", "tripTime");
    }
}