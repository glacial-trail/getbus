package info.getbus.servebus.persistence.datamappers.route;

import info.getbus.servebus.model.periodicity.Periodicity;
import info.getbus.servebus.model.route.Route;
import info.getbus.servebus.model.route.WayPoint;
import info.getbus.servebus.model.security.User;
import info.getbus.servebus.persistence.datamappers.transporter.TransporterAreaMapper;
import info.getbus.servebus.persistence.datamappers.transporter.TransporterMapper;
import info.getbus.servebus.service.transporter.TransporterService;
import info.getbus.servebus.service.transporter.TransporterServiceImpl;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.function.Supplier;

import static io.github.benas.randombeans.FieldDefinitionBuilder.field;
import static io.github.benas.randombeans.api.EnhancedRandom.random;

@Ignore
public class RouteAwareBaseTest {
    @Bean
    public TransporterService transporterService(
            @Autowired TransporterAreaMapper transporterAreaMapper,
            @Autowired TransporterMapper transporterMapper
    ) {
        return new TransporterServiceImpl(transporterAreaMapper, transporterMapper);
    }

    protected User user;
    protected Route route;

    protected EnhancedRandom routeAwareRandomizer = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .objectPoolSize(1000)
            .exclude(field().named("id").ofType(Long.class).inClass(Route.class).get())
            .exclude(field().named("version").ofType(Integer.class).inClass(Route.class).get())
//            .exclude(field().named("direction").ofType(Integer.class).inClass(Route.class).get())
            .exclude(field().named("id").ofType(Long.class).inClass(WayPoint.class).get())
            .overrideDefaultInitialization(true)
            .collectionSizeRange(14,22)
            .randomize(field().named("basePrice").ofType(BigDecimal.class).inClass(Route.class).get(),
                    (Supplier<BigDecimal>) () -> BigDecimal.valueOf((double)new Random().nextInt(10000)/10))
            .randomize(field().named("startSales").ofType(ZonedDateTime.class).inClass(Route.class).get(),
                    (Supplier<ZonedDateTime>) () ->  ZonedDateTime.of(random(LocalDateTime.class), ZoneId.of("UTC")))
            .randomize(field().named("start").ofType(ZonedDateTime.class).inClass(Periodicity.class).get(),
                    (Supplier<ZonedDateTime>) () ->  ZonedDateTime.of(random(LocalDateTime.class), ZoneId.of("UTC")))
            .randomize(field().named("countryCode").ofType(String.class).inClass(WayPoint.class).get(),
                    (Supplier<String>) () ->  "UA")
            .build();

    protected Route newRoute() {
        return routeAwareRandomizer.nextObject(Route.class);
    }


    private EnhancedRandom stringRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .stringLengthRange(8, 12)
            .build();

    protected User newUser() {
        return newUser(null);
    }
    protected User newUser(String username) {
        if (null == username) {
            username = random(String.class);
        }
        return new User(username, random(String.class), true, random(String.class), random(String.class), stringRandom.nextObject(String.class));
    }

    @Before
    public final void setUpUserAndRoute() {
        user = newUser();
        route = newRoute();
    }
}