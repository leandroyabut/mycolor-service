package com.arjay07.mycolorservice.exception.user;

import com.arjay07.mycolorservice.exception.BadRequestException;

public class UsernameExistsException extends BadRequestException {
    public UsernameExistsException() {
        super("Username already exists.");
    }
}
