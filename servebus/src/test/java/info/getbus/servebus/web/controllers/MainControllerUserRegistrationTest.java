package info.getbus.servebus.web.controllers;

import info.getbus.servebus.dao.security.UserMapper;
import info.getbus.servebus.model.security.User;
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

import java.util.Collections;

import static info.getbus.test.springframework.web.servlet.result.ModelResultMatchers.attributeHasNoFieldErrors;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {MainControllerUserRegistrationTest.class})
@ImportResource("classpath:web-config.xml")
@WebAppConfiguration
@Configuration
@ComponentScan(
        basePackages = "info.getbus.servebus.web",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {MainController.class})
)
class MainControllerUserRegistrationTest {
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
                .alwaysExpect(status().isOk())// TODO is this correct?
                .alwaysExpect(view().name("register-partner"))
                .build();
        reset(userMapper, registrationService);
    }

    @Test
    void verifyUniqueUser_positive() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("email", "dirty@email.com")
                .param("phone", "+3801234567")
        )
                .andExpect(model().hasErrors())
                .andExpect(attributeHasNoFieldErrors("user", "email", "phone"))
                .andExpect(view().name("register-partner"));
        verify(userMapper).selectByUsernameOrPhone(eq("dirty@email.com"), eq("+3801234567"));
    }

    @Test
    void verifyUniqueUser_negative() throws Exception {
        User user = new User("dirty@email.com", "", true, "", "",
                "+3801234567");

        when(userMapper.selectByUsernameOrPhone(any(), any())).thenReturn(Collections.singleton(user));
        mockMvc.perform(post("/register-partner")
                .param("email", "dirty@email.com")
                .param("phone", "+3801234567")
        )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "email", "phone"))
                .andExpect(view().name("register-partner"));
        verify(userMapper).selectByUsernameOrPhone(eq("dirty@email.com"), eq("+3801234567"));
    }


    @Test
    void verifyEmail_positive() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("email", "dirty@email.com")
        )
                .andExpect(attributeHasNoFieldErrors("user", "email"));
    }

    @Test
    void verifyEmail_negative() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("email", "dirtmail.com")
        )
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }

    @Test
    void verifyFirstName_positive() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("firstname", "Huilo")
        )
                .andExpect(attributeHasNoFieldErrors("user", "firstname"));
    }

    @Test
    void verifyFirstName_negative_null() throws Exception {
        mockMvc.perform(post("/register-partner")
                //.param("name", "")
        )
                .andExpect(model().attributeHasFieldErrors("user", "firstname"));
    }

    @Test
    void verifyFirstName_negative_empty() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("firstname", "")
        )
                .andExpect(model().attributeHasFieldErrors("user", "firstname"));
    }

    @Test
    void verifyLastName_positive() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("lastname", "Huilo")
        )
                .andExpect(attributeHasNoFieldErrors("user", "lastname"));
    }

    @Test
    void verifyLastName_negative_null() throws Exception {
        mockMvc.perform(post("/register-partner"))
                .andExpect(model().attributeHasFieldErrors("user", "lastname"));
    }

    @Test
    void verifyLastName_negative_empty() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("lastname", "")
        )
                .andExpect(model().attributeHasFieldErrors("user", "lastname"));
    }

    @Test
    void verifyPhone_positive() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("phone", "+380648204581") //"Redesign regexp according to our needs
        )
                .andExpect(attributeHasNoFieldErrors("user", "phone"));
    }

    @Test
    void verifyPhone_negative() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("phone", "0667651234")
        )
                .andExpect(model().attributeHasFieldErrors("user", "phone"));
    }
    @Test
    void verifyPassword_positive() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("email", "dirty@gmail.com")
                .param("phone", "+380648204581")
                .param("name", "Jorje")
                .param("lastname", "dirty")
                .param("password", "908345")
        )
                .andExpect(attributeHasNoFieldErrors("user", "password"));
    }


    @Test
    void verifyPassword_negative() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("email", "dirty@gmail.com")
                .param("phone", "+380648204581")
                .param("name", "Jorje")
                .param("lastname", "dirty")
                .param("password", "90834")
        )
                .andExpect(model().attributeHasFieldErrors("user", "password"));
    }
    @Test
    void verifyRePassword_positive() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("email", "dirty@gmail.com")
                .param("phone", "+380648204581")
                .param("name", "Jorje")
                .param("lastname", "dirty")
                .param("password", "908345")
                .param("repassword", "908345")
        )
                .andExpect(attributeHasNoFieldErrors("user", "repassword"));
    }


    @Test
    void verifyRePassword_negative() throws Exception {
        mockMvc.perform(post("/register-partner")
                .param("email", "dirty@gmail.com")
                .param("phone", "+380648204581")
                .param("name", "Jorje")
                .param("lastname", "dirty")
                .param("password", "908341")
                .param("repassword", "908345")
        )
                .andExpect(model().attributeHasFieldErrors("user", "repassword"));
    }
}