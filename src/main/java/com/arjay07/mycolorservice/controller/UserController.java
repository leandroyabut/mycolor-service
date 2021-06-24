package com.arjay07.mycolorservice.controller;

import com.arjay07.mycolorservice.dto.RegistrationDTO;
import com.arjay07.mycolorservice.model.User;
import com.arjay07.mycolorservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${server.port}")
    private int port;

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUserByUsername(username));
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegistrationDTO registration) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .port(port)
                .path("/users/{username}")
                .buildAndExpand(registration.getUsername())
                .toUri();
        return ResponseEntity
                .created(location)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.registerUser(registration));
    }

}
