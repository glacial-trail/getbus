package info.getbus.servebus.route.persistence.mappers;

import info.getbus.servebus.route.model.Periodicity;
import info.getbus.servebus.route.model.Direction;
import info.getbus.servebus.route.model.RoutePartId;
import info.getbus.servebus.route.model.RoutePeriodicity;
import info.getbus.test.util.Container;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static info.getbus.servebus.route.model.Direction.F;
import static info.getbus.servebus.route.model.Direction.R;
import static info.getbus.test.util.Assertions.assertThatObjectsAreEqualUsingFields;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class RoutePeriodicityMapperTest extends RouteAwarePersistenceBaseTest {

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
        assertThat(actual.getRoutePartId(), is(notNullValue()));
        assertThat(actual.getPeriodicity(), is(notNullValue()));
        assertThat(actual.getRoutePartId().getId(), is(route.getId()));
        assertThat(directionOf(actual), is(F));
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
        expIdContainer.put(F, expForward);
        Periodicity expReverse = newPeriodicity();
        periodicityMapper.insert(RoutePartId.reverse(route.getId()), expReverse);
        expIdContainer.put(R, expReverse);

        List<RoutePeriodicity> actualPair = periodicityMapper.selectByRouteId(route.getId());
        assertThat(actualPair, hasSize(2));
        assertThat(actualPair.stream()
                .map(this::directionOf).collect(Collectors.toSet()), containsInAnyOrder(F,R));
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
        assertThat(route.getId(), is(actual.getRoutePartId().getId()));
        assertThat(F, is(directionOf(actual)));
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