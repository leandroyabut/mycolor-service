package com.arjay07.mycolorservice.controller;

import com.arjay07.mycolorservice.dto.RegistrationDTO;
import com.arjay07.mycolorservice.exception.user.UserNotFoundException;
import com.arjay07.mycolorservice.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
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
    void test_registerUser_status_is_badRequest_when_username_already_exists() {
        RegistrationDTO registration = RegistrationDTO.builder()
                .username("testboy")
                .email("user@test.com")
                .name("New User")
                .password("P@ssword123")
                .build();



    }
}
