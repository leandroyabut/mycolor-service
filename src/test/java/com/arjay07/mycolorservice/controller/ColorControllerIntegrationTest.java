package com.arjay07.mycolorservice.controller;

import com.arjay07.mycolorservice.dto.PostColorDTO;
import com.arjay07.mycolorservice.exception.color.ColorNameExistsException;
import com.arjay07.mycolorservice.exception.color.HexExistsException;
import com.arjay07.mycolorservice.repository.ColorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.Objects;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ColorControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ColorRepository repository;

    @Autowired
    ObjectMapper mapper;

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
    void test_getColors_status_is_ok_and_contains_five_colors_when_search_is_00() throws Exception {
        mvc.perform(get("/colors?search=ff"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content[3].id").value(4))
                .andExpect(jsonPath("$.content[4].id").value(5));
    }

    @Test
    void test_getColors_status_is_ok_and_content_is_empty_when_search_does_not_match_any_colors() throws Exception {
        mvc.perform(get("/colors?search=x"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.empty").value(true))
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void test_postColor_status_is_created_and_location_is_in_header() throws Exception {
        PostColorDTO postColor = PostColorDTO.builder()
                .name("Cosmic Latte")
                .hex("fff8e7").build();
        String body = mapper.writeValueAsString(postColor);
        MvcResult result = mvc.perform(post("/colors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/colors/7")))
                .andReturn();

        URI location = URI.create((String) Objects.requireNonNull(result.getResponse().getHeaderValue("location")));

        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Cosmic Latte"))
                .andExpect(jsonPath("$.hex").value("fff8e7"));
    }

    @Test
    void test_postColor_status_is_badRequest_when_postColorDTO_is_invalid() throws Exception {
        PostColorDTO postColor = PostColorDTO.builder()
                .name("No Color")
                .hex("xxxxxx").build();
        String body = mapper.writeValueAsString(postColor);
        mvc.perform(post("/colors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("not in the correct format")));
    }

    @Test
    void test_postColor_status_is_badRequest_when_color_name_exists() throws Exception {
        PostColorDTO postColor = PostColorDTO.builder()
                .name("Green")
                .hex("02ff00").build();
        String body = mapper.writeValueAsString(postColor);
        mvc.perform(post("/colors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ColorNameExistsException().getMessage()));
    }

    @Test
    void test_postColor_status_is_badRequest_when_color_hex_exists() throws Exception {
        PostColorDTO postColor = PostColorDTO.builder()
                .name("Dark Blue")
                .hex("0000ff").build();
        String body = mapper.writeValueAsString(postColor);
        mvc.perform(post("/colors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new HexExistsException().getMessage()));
    }

}
