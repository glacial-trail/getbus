package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.periodicity.Periodicity;
import info.getbus.servebus.model.route.Direction;
import info.getbus.servebus.model.route.RoutePartId;
import info.getbus.servebus.model.route.RoutePeriodicity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class RoutePeriodicityMapperTest extends RouteAwareBaseTest {

    @Autowired
    private RoutePeriodicityMapper periodicityMapper;

    @Before
    public final void setUpRoute() {
        routeMapper.insertLocked(transporterAreaId, route, user.getUsername());
    }

    @Test
    public void insertAndSelect() throws Exception {
        Periodicity expected = newPeriodicity();
        periodicityMapper.insert(RoutePartId.forward(route.getId()), expected);
        assertThat(expected.getId(), is(notNullValue()));

        List<RoutePeriodicity> periodicity = periodicityMapper.selectByRouteId(route.getId());
        assertThat(periodicity, hasSize(1));
        RoutePeriodicity actual = periodicity.iterator().next();
        assertNotNull(actual.getRoutePartId());
        assertNotNull(actual.getPeriodicity());
        assertThat(actual.getRoutePartId().getId(), is(route.getId()));
        assertThat(directionOf(actual), is(Direction.F));
        assertThatPeriodicitiesAreEqual(actual, expected);
    }

    @Test
    public void select() throws Exception {
        class PeriodicityContainer extends Container<Direction, Periodicity, Long> {
            PeriodicityContainer() {
                super(null, Periodicity::getId);
            }
            Long getFor(RoutePeriodicity routePeriodicity) {
                return getByKey(directionOf(routePeriodicity));
            }
        }
        PeriodicityContainer expIdContainer = new PeriodicityContainer();

        Periodicity expForward = newPeriodicity();
        periodicityMapper.insert(RoutePartId.forward(route.getId()), expForward);
        expIdContainer.put(Direction.F, expForward);
        Periodicity expReverse = newPeriodicity();
        periodicityMapper.insert(RoutePartId.reverse(route.getId()), expReverse);
        expIdContainer.put(Direction.R, expReverse);

        List<RoutePeriodicity> actualPair = periodicityMapper.selectByRouteId(route.getId());
        assertThat(actualPair, hasSize(2));
        assertThat(actualPair.stream()
                .map(this::directionOf)
                .distinct().count(),
                is(2L));
        actualPair.forEach(actPeriodicity ->
                assertThat(idOf(actPeriodicity), is(expIdContainer.getFor(actPeriodicity)))
        );
    }

    @Test
    public void updatePeriodicity() throws Exception {
        Periodicity periodicity = newPeriodicity();
        periodicityMapper.insert(RoutePartId.forward(route.getId()), periodicity);
        Periodicity expected = newPeriodicity();
        expected.setId(periodicity.getId());
        periodicityMapper.update(route.getId(), expected);
        RoutePeriodicity actual = periodicityMapper.selectByRouteId(route.getId()).iterator().next();
        assertEquals(route.getId(), actual.getRoutePartId().getId());
        assertEquals(Direction.F, directionOf(actual));
        assertThatPeriodicitiesAreEqual(actual, expected);
    }

    private Periodicity newPeriodicity() {
        return routeAwareRandomizer.nextObject(Periodicity.class);
    }

    private Long idOf(RoutePeriodicity uno) {
        return uno.getPeriodicity().getId();
    }

    private Direction directionOf(RoutePeriodicity routePeriodicity) {
        return routePeriodicity.getRoutePartId().getDirection();
    }

    private void assertThatPeriodicitiesAreEqual(RoutePeriodicity actual, Periodicity expected) {
        assertThatObjectsAreEqualUsingFields(actual.getPeriodicity(), expected,
                "id", "start", "strategy", "param");
    }
}