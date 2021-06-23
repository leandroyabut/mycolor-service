package com.arjay07.mycolorservice.service;

import com.arjay07.mycolorservice.exception.user.UserNotFoundException;
import com.arjay07.mycolorservice.model.User;
import com.arjay07.mycolorservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findById(username).orElseThrow(UserNotFoundException::new);
    }

}
