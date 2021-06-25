package com.arjay07.mycolorservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    void test_getColors_status_is_ok_and_content_is_not_empty() throws Exception {
        mvc.perform(get("/colors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.empty").value(false));
    }

    @Test
    void test_getColors_status_is_ok_and_contains_red_and_dark_red_color_when_search_is_red() throws Exception {
        mvc.perform(get("/colors?search=red"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Red"))
                .andExpect(jsonPath("$.content[0].hex").value("ff0000"))
                .andExpect(jsonPath("$.content[1].id").value(6))
                .andExpect(jsonPath("$.content[1].name").value("Dark Red"))
                .andExpect(jsonPath("$.content[1].hex").value("800800"))
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void test_getColors_status_is_ok_and_contains_red_when_search_is_ff0000() throws Exception {
        mvc.perform(get("/colors?search=ff0000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Red"))
                .andExpect(jsonPath("$.content[0].hex").value("ff0000"))
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void test_getColors_status_is_ok_and_contains_five_colors_when_search_is_ff() throws Exception {
        mvc.perform(get("/colors?search=ff"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content[3].id").value(4))
                .andExpect(jsonPath("$.content[4].id").value(5))
                .andExpect(jsonPath("$.content.length()").value(5));
    }

    @Test
    void test_getColors_status_is_ok_and_content_is_empty_when_search_does_not_match_any_colors() throws Exception {
        mvc.perform(get("/colors?search=x"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.empty").value(true))
                .andExpect(jsonPath("$.content").isEmpty());
    }

}
