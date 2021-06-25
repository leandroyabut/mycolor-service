package com.arjay07.mycolorservice.exception.color;

import com.arjay07.mycolorservice.exception.BadRequestException;

public class HexExistsException extends BadRequestException {
    public HexExistsException() {
        super("Color with that hex code already exists.");
    }
}
