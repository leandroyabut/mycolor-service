package com.arjay07.mycolorservice.service;

import com.arjay07.mycolorservice.exception.color.ColorNotFoundException;
import com.arjay07.mycolorservice.model.Color;
import com.arjay07.mycolorservice.repository.ColorRepository;
import com.arjay07.mycolorservice.util.ColorSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
class ColorServiceTest {

    @Autowired
    private ColorService colorService;

    @MockBean
    private ColorRepository colorRepository;

    private List<Color> colorList;

    @BeforeEach
    void setUp() {

        Color red = Color.builder()
                .id(1)
                .hex("ff0000")
                .name("Red").build();

        Color green = Color.builder()
                .id(2)
                .hex("00ff00")
                .name("Green").build();

        Color blue = Color.builder()
                .id(3)
                .hex("0000ff")
                .name("Blue").build();

        colorList = new ArrayList<>();
        colorList.add(red);
        colorList.add(green);
        colorList.add(blue);
        Page<Color> colorPage = new PageImpl<>(colorList);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        ColorSpecification colorSpec = new ColorSpecification("");

        when(colorRepository.findById(1)).thenReturn(Optional.of(red));
        when(colorRepository.findById(2)).thenThrow(ColorNotFoundException.class);
        when(colorRepository.findAll(colorSpec, pageable)).thenReturn(colorPage);
    }

    @Test
    void test_getColorById_returns_correct_color() {
        Color color = colorService.getColorById(1);
        assertEquals(color.getId(), 1);
        assertEquals(color.getHex(), "ff0000");
        assertEquals(color.getName(), "Red");
    }

    @Test
    void test_getColorById_throws_colorNotFoundException() {
        assertThrows(ColorNotFoundException.class, () -> colorService.getColorById(2));
    }

    @Test
    void test_getColors_returns_colorList_in_content() {
        Page<Color> colors = colorService.getColors(0, 10, "id", "", "");
        for (int i = 0; i < colorList.size(); i++) {
            assertEquals(colorList.get(i), colors.getContent().get(i));
        }
    }

}
