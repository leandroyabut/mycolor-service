package com.arjay07.userservice.exception.user;

import com.arjay07.userservice.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("User does not exist.");
    }
}
