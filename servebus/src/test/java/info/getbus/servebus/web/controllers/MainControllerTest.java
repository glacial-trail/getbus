package info.getbus.servebus.web.controllers;

import info.getbus.servebus.dao.security.UserMapper;
import info.getbus.servebus.model.security.RegisterUserDTO;
import info.getbus.servebus.service.security.RegistrationService;
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

import static info.getbus.test.harmcrest.object.IsEqualsUsingReflection.recursivelyEqualsTo;
import static info.getbus.test.mockito.ArgumentMatchers.req;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

//@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {MainControllerTest.class})
@ImportResource("classpath:web-config.xml")
@WebAppConfiguration
@Configuration
@ComponentScan(
        basePackages = "info.getbus.servebus.web",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {MainController.class}))
class MainControllerTest {
    @Bean
    public UserMapper userMapper() {
        return mock(UserMapper.class);
    }

    @Bean
    public RegistrationService registrationService() {
        return mock(RegistrationService.class);
    }

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RegistrationService registrationService;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
//                .standaloneSetup(new MainController(registrationService))
                .build();
        reset(userMapper, registrationService);

    }

    @Test
    void registerPartner() throws Exception {
        mockMvc.perform(get("/register-partner"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-partner"))
                .andExpect(model().attribute("user", recursivelyEqualsTo(new RegisterUserDTO())));
    }

    @Test
    void registerTransporter_positive() throws Exception {
        RegisterUserDTO user = new RegisterUserDTO();
        user.setFirstname("jorge");
        user.setLastname("dirty");
        user.setEmail("dirty@email.com");
        user.setPhone("+3801234567");
        user.setPassword("dirty123");
        user.setRepassword("dirty123");

        mockMvc.perform(post("/register-partner")
                .param("firstname", user.getFirstname())
                .param("lastname", user.getLastname())
                .param("email", user.getEmail())
                .param("phone", user.getPhone())
                .param("password", user.getPassword())
                .param("repassword", user.getRepassword())
        )
                .andExpect(model().hasNoErrors())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
        verify(registrationService).registerTransporter(req(user));
    }

    @Test
    void registerTransporter_negative() throws Exception {
        mockMvc.perform(post("/register-partner"))
                .andExpect(model().hasErrors())
                .andExpect(status().isOk())
                .andExpect(view().name("register-partner"))
                .andExpect(model().attribute("user", recursivelyEqualsTo(new RegisterUserDTO())));
        verifyZeroInteractions(registrationService);
    }

}