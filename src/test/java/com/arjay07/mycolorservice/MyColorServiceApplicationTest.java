package com.arjay07.mycolorservice;

import com.arjay07.mycolorservice.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestConfig.class)
@SpringBootTest
class MyColorServiceApplicationTest {

    @Autowired
    private MyColorServiceApplication app;

    @Autowired
    private ObjectMapper mapper1;

    @Autowired
    private ObjectMapper mapper2;

    @Test
    void contextLoads() {
        assertNotNull(app);
    }

    @Test
    void testNotSingletonMapper() {
        assertNotEquals(mapper1, mapper2);
    }

}
