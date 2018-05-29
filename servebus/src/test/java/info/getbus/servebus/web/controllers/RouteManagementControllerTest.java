package info.getbus.servebus.web.controllers;

import info.getbus.servebus.geo.address.persistence.CountriesRepository;
import info.getbus.servebus.route.RouteService;
import info.getbus.servebus.service.security.SecurityHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {RouteManagementControllerTest.class})
@ImportResource({"classpath:web-config.xml", "classpath:conversion-config.xml"})
@WebAppConfiguration
@Configuration
@ComponentScan(
        basePackages = "info.getbus.servebus.web",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {RouteManagementController.class}))
class RouteManagementControllerTest {
    @Bean
    public RouteService registrationService() {
        return mock(RouteService.class);
    }
    @Bean
    public SecurityHelper securityHelper() {
        return mock(SecurityHelper.class);
    }
    @Bean
    public CountriesRepository countriesRepository() {
        return mock(CountriesRepository.class);
    }

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
    }

    @Test
    void registerPartner() throws Exception {
        mockMvc.perform(get("/t/routes/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("tr/cab"))
        ;
    }

}