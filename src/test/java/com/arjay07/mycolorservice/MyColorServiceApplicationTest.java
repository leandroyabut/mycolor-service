package com.arjay07.mycolorservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyColorServiceApplicationTest {

    @Autowired
    private MyColorServiceApplication app;

    @Test
    void contextLoads() {
        assertNotNull(app);
    }

}
