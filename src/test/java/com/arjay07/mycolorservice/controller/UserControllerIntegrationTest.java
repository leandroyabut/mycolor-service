package com.arjay07.mycolorservice.controller;

import com.arjay07.mycolorservice.dto.RegistrationDTO;
import com.arjay07.mycolorservice.exception.user.UserNotFoundException;
import com.arjay07.mycolorservice.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void test_getUserByUsername_status_is_ok_when_user_exists() throws Exception {
        mockMvc.perform(get("/users/testboy"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void test_getUserByUsername_status_is_notFound_when_user_does_not_exists() throws Exception {
        mockMvc.perform(get("/users/nothing"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(new UserNotFoundException().getMessage()))
                .andExpect(content().contentType(MediaType.TEXT_PLAIN));
    }

    @Test
    void test_registerUser_status_is_created_and_location_is_in_header_when_registration_is_successful() throws Exception {
        RegistrationDTO registration = RegistrationDTO.builder()
                .username("new_user")
                .email("user@test.com")
                .name("New User")
                .password("P@ssword123")
                .build();

        String body = mapper.writeValueAsString(registration);

        MvcResult result = mockMvc.perform(post("/users/registration")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location",
                        containsString(String.format("/users/%s", registration.getUsername()))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("new_user"))
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.name").value("New User"))
                .andReturn();

        String hashed = mapper.readValue(
                result.getResponse().getContentAsString(),
                User.class).getPassword();

        assertTrue(encoder.matches(registration.getPassword(), hashed));
    }

    @Test
    void test_registerUser_status_is_badRequest_when_password_is_too_short() throws Exception {
        RegistrationDTO registration = RegistrationDTO.builder()
                .username("new_user_1")
                .email("user@test.com")
                .name("New User")
                .password("pswrd")
                .build();

        String body = mapper.writeValueAsString(registration);

        mockMvc.perform(post("/users/registration")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("password"))
                .andExpect(jsonPath("$.message").value(containsString("Password must be at least")));
    }

    @Test
    void test_registerUser_status_is_badRequest_when_password_is_too_long() throws Exception {
        String longPassword = IntStream.range(0, 100 ).mapToObj(i -> "x").collect(Collectors.joining());
        RegistrationDTO registration = RegistrationDTO.builder()
                .username("new_user_1")
                .email("user@test.com")
                .name("New User")
                .password(longPassword) // Too long
                .build();

        String body = mapper.writeValueAsString(registration);

        mockMvc.perform(post("/users/registration")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("password"))
                .andExpect(jsonPath("$.message").value(containsString("Password must be no more than")));
    }

    @Test
    void test_registerUser_status_is_badRequest_when_username_already_exists() throws Exception {
        RegistrationDTO registration = RegistrationDTO.builder()
                .username("testboy")
                .email("testboy@test.com")
                .name("Test Boy")
                .password("P@ssword123")
                .build();

        String body = mapper.writeValueAsString(registration);

        mockMvc.perform(post("/users/registration")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Username already exists."));
    }

    @Test
    void test_registerUser_status_is_badRequest_when_email_already_exists() throws Exception {
        RegistrationDTO registration = RegistrationDTO.builder()
                .username("testboy_1")
                .email("testboy@test.com")
                .name("Test Boy")
                .password("P@ssword123")
                .build();

        String body = mapper.writeValueAsString(registration);

        mockMvc.perform(post("/users/registration")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Email already exists."));
    }

    @Test
    void test_registerUser_status_is_created_and_user_exists_in_location_from_header_status_is_ok() throws Exception {
        RegistrationDTO registration = RegistrationDTO.builder()
                .username("test_boy")
                .email("test_boy@test.com")
                .name("Test Boy")
                .password("P@ssword123")
                .build();

        String body = mapper.writeValueAsString(registration);

        MvcResult result = mockMvc.perform(post("/users/registration")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location",
                        containsString(String.format("/users/%s", registration.getUsername()))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("test_boy"))
                .andExpect(jsonPath("$.email").value("test_boy@test.com"))
                .andExpect(jsonPath("$.name").value("Test Boy"))
                .andReturn();

        String hashed = mapper.readValue(
                result.getResponse().getContentAsString(),
                User.class).getPassword();

        assertTrue(encoder.matches(registration.getPassword(), hashed));

        String location = (String) result.getResponse().getHeaderValue("location");
        assertNotNull(location);

        mockMvc.perform(get(URI.create(location)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("test_boy"));
    }
}
