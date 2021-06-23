package com.arjay07.mycolorservice.exception.user;

import com.arjay07.mycolorservice.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("User does not exist.");
    }
}
