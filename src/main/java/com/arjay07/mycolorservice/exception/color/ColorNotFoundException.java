package com.arjay07.mycolorservice.exception.color;

import com.arjay07.mycolorservice.exception.NotFoundException;

public class ColorNotFoundException extends NotFoundException {
    public ColorNotFoundException() {
        super("Color does not exist.");
    }
}
