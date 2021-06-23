package com.arjay07.mycolorservice.service;

import com.arjay07.mycolorservice.exception.user.UserNotFoundException;
import com.arjay07.mycolorservice.model.User;
import com.arjay07.mycolorservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User testboy = User.builder()
                .name("Test Boy")
                .username("testboy")
                .email("testboy@test.com")
                .password("password").build();
        when(userRepository.findById("testboy")).thenReturn(Optional.of(testboy));
        when(userRepository.findById("nothing")).thenThrow(UserNotFoundException.class);
    }

    @Test
    void test_getUserByUsername_returns_correct_user_when_user_exists() {
        User user = userService.getUserByUsername("testboy");

        assertEquals(user.getUsername(), "testboy");
        assertEquals(user.getName(), "Test Boy");
        assertEquals(user.getEmail(), "testboy@test.com");
        assertEquals(user.getPassword(), "password");
    }

    @Test
    void test_getUserByUsername_throws_UserNotFoundException_when_user_does_not_exist() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("nothing"));
    }

    @Test
    void test_registerUser_returns_user_when_registration_is_successful() {

    }

}
