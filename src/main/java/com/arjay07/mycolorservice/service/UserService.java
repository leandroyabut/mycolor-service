package com.arjay07.mycolorservice.service;

import com.arjay07.mycolorservice.dto.RegistrationDTO;
import com.arjay07.mycolorservice.exception.user.EmailExistsException;
import com.arjay07.mycolorservice.exception.user.UserNotFoundException;
import com.arjay07.mycolorservice.exception.user.UsernameExistsException;
import com.arjay07.mycolorservice.model.User;
import com.arjay07.mycolorservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        if (userRepository.existsByUsername(registration.getUsername()))
            throw new UsernameExistsException();
        if (userRepository.existsByEmail(registration.getEmail()))
            throw new EmailExistsException();

        User user = modelMapper.map(registration, User.class);
        String password = encoder.encode(registration.getPassword());
        user.setPassword(password);
        return userRepository.save(user);
    }

}
