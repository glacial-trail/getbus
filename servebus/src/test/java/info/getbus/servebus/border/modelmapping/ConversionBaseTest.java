package info.getbus.servebus.border.modelmapping;

import info.getbus.servebus.model.security.User;
import info.getbus.servebus.route.persistence.RouteAwareBaseTest;
import info.getbus.servebus.service.security.SecurityHelper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Cfg.class})
class ConversionBaseTest {
    @Autowired
    protected ModelMapper modelMapper;
    private static final EnhancedRandom rnd = EnhancedRandomBuilder
            .aNewEnhancedRandomBuilder()
            .overrideDefaultInitialization(true)
            .build();

    @Mock
    protected User user;
    @MockBean
    private SecurityHelper securityHelper;

    protected void setUpSecurityHelper() {
        when(securityHelper.getCurrentUser()).thenReturn(user);
    }

    protected void validateMapping(Class<?> srcClass, Class<?> dstClass) {
        Object src = rnd.nextObject(srcClass);
        Object dst = modelMapper.map(src, dstClass);
        modelMapper.validate();
    }
}