package com.arjay07.mycolorservice.exception.user;

import com.arjay07.mycolorservice.exception.BadRequestException;

public class EmailExistsException extends BadRequestException {
    public EmailExistsException() {
        super("Email already exists.");
    }
}
