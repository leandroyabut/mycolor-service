package com.arjay07.mycolorservice.service;

import com.arjay07.mycolorservice.dto.RegistrationDTO;
import com.arjay07.mycolorservice.exception.user.UserNotFoundException;
import com.arjay07.mycolorservice.model.User;
import com.arjay07.mycolorservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    public User getUserByUsername(String username) {
        return userRepository.findById(username).orElseThrow(UserNotFoundException::new);
    }

    public User registerUser(RegistrationDTO registration) {
        User user = modelMapper.map(registration, User.class);
        String password = encoder.encode(registration.getPassword());
        user.setPassword(password);
        return userRepository.save(user);
    }

}
