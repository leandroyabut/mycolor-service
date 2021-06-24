package com.arjay07.mycolorservice.controller;

import com.arjay07.mycolorservice.model.Color;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/colors")
public class ColorController {

    @GetMapping("/{colorName}")
    public Color getColorByName(String name)

}
