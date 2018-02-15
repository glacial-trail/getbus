package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.dao.security.UserMapper;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.WayPoint;
import info.getbus.servebus.persistence.datamappers.transporter.TransporterAreaMapper;
import info.getbus.servebus.persistence.datamappers.transporter.TransporterMapper;
import info.getbus.servebus.service.transporter.TransporterService;
import info.getbus.servebus.service.transporter.TransporterServiceImpl;
import lombok.SneakyThrows;
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

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    //TODO implement hamcrest matcher
    @SneakyThrows
    protected void assertThatObjectsAreEqualUsingFields(Object oAct, Object oExp, String... fields) {
        for (String field : fields) {
            Object actual = new PropertyDescriptor(field, oAct.getClass()).getReadMethod().invoke(oAct);
            Object expected = new PropertyDescriptor(field, oExp.getClass()).getReadMethod().invoke(oExp);
            assertNotNull(actual);
            if (expected instanceof Comparable) {
                //noinspection unchecked
                assertEquals(field,0, ((Comparable)actual).compareTo(expected));
            } else {
                assertEquals(field, expected, actual);
            }
        }
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

    class SelfContainer<K,O> extends Container<K,O,O> {
        public SelfContainer(Function<O, K> keyFunc) {
            super(keyFunc, o -> o);
        }
    }
    class Container<K,O,V> {
        private Function<O, K> keyFunc;
        private Function<O, V> valFunc;
        private Map<K, V> entities = new HashMap<>();

        public Container(Function<O, K> keyFunc, Function<O, V> valFunc) {
            this.keyFunc = keyFunc;
            this.valFunc = valFunc;
        }

        public void put(O entity) {
            entities.put(keyFunc.apply(entity), valFunc.apply(entity));
        }
        public void put(K key, O entity) {
            entities.put(key, valFunc.apply(entity));
        }
        public V getFor(O entity) {
            assertTrue(entities.containsKey(keyFunc.apply(entity)));
            return entities.get(keyFunc.apply(entity));
        }
        public V getByKey(K key) {
            return entities.get(key);
        }
    }

    static class DoubleFor<T> {
        private Iterable<T> uno;
        private Iterable<T> dos;

        public DoubleFor(Iterable<T> uno, Iterable<T> dos) {
            this.uno = uno;
            this.dos = dos;
        }

        public void iterate(BiConsumer<? super T, ? super T> action) {
            Objects.requireNonNull(action);
            Iterator<T> itUno = uno.iterator();
            Iterator<T> itDos = dos.iterator();
            while (itUno.hasNext()) {
                if (!itDos.hasNext()) {
                    fail("The size of iterables are different. Uno bigger");
                }
                T elementOfUno = itUno.next();
                T elementOfDos = itDos.next();
                action.accept(elementOfUno, elementOfDos);
            }
            if (itDos.hasNext()) {
                fail("The size of iterables are different. Dos bigger");
            }
        }
    }
}