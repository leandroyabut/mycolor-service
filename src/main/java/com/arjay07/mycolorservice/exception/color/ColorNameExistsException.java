package com.arjay07.mycolorservice.exception.color;

import com.arjay07.mycolorservice.exception.BadRequestException;

public class ColorNameExistsException extends BadRequestException {
    public ColorNameExistsException() {
        super("A color with that name already exists.");
    }
}
