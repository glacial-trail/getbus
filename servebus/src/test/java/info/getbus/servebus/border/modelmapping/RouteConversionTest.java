package info.getbus.servebus.border.modelmapping;

import info.getbus.servebus.model.security.User;
import info.getbus.servebus.model.user.Profile;
import info.getbus.servebus.route.model.Route;
import info.getbus.servebus.route.model.WayPoint;
import info.getbus.servebus.route.persistence.RouteAwareBaseTest;
import info.getbus.servebus.service.security.SecurityHelper;
import info.getbus.servebus.web.dto.route.RouteDTO;
import info.getbus.servebus.web.dto.route.StopDTO;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZoneId;

import static org.mockito.Mockito.when;

@TestConfiguration
@ImportResource("classpath:conversion-config.xml")
@ComponentScan(basePackages = "info.getbus.servebus.border.modelmapping")
class Cfg {
}

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Cfg.class})
class RouteConversionTest extends RouteAwareBaseTest {
    private static final EnhancedRandom rnd = EnhancedRandomBuilder
            .aNewEnhancedRandomBuilder()
            .overrideDefaultInitialization(true)
            .build();

    @Mock
    User user;
    @MockBean
    private SecurityHelper securityHelper;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        Profile p = new Profile();
        p.setTimeZone(ZoneId.of("GMT"));
        when(user.getProfile()).thenReturn(p);
        when(securityHelper.getCurrentUser()).thenReturn(user);
    }

    @Test
    void dtoToRoute() {
        new ModelMapperValidationTemplate<>(RouteDTO.class, Route.class).validate();
    }

    @Test
    void routeToDto() {
        new ModelMapperValidationTemplate<>(Route.class, RouteDTO.class).validate();
    }

    @Test
    void dtoToStop() {
        new ModelMapperValidationTemplate<>(StopDTO.class, WayPoint.class).validate();
    }

    @Test
    void stopToDto() {
        new ModelMapperValidationTemplate<>(WayPoint.class, StopDTO.class).validate();
    }


    @RequiredArgsConstructor
    private class ModelMapperValidationTemplate<S,D> {
        private final Class<S> srcClass;
        private final Class<D> dstClass;

    public void validate() {
            S src = rnd.nextObject(srcClass);
            modelMapper.map(src, dstClass);
            modelMapper.validate();
        }
    }
}