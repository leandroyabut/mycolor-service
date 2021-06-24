package com.arjay07.mycolorservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ColorControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void test_getColorById_status_is_ok_when_color_exists() throws Exception {
        mvc.perform(get("/colors/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Red"))
                .andExpect(jsonPath("$.hex").value("ff0000"));
    }

    @Test
    void test_getColorByName_status_is_ok_when_color_exists() throws Exception {
        mvc.perform(get("/colors/name/red"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Red"))
                .andExpect(jsonPath("$.hex").value("ff0000"));
    }

    @Test
    void test_getColorByHex_status_is_ok_when_color_exists() throws Exception {
        mvc.perform(get("/colors/hex/ff0000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Red"))
                .andExpect(jsonPath("$.hex").value("ff0000"));
    }

}
