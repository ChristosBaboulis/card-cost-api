package com.example.card_cost_api.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class AuthRequestTest {
    @Test
    public void testNoArgsConstructor() {
        AuthRequest authRequest = new AuthRequest();
        assertNull(authRequest.getUsername());
        assertNull(authRequest.getPassword());
    }

    @Test
    public void testAllArgsConstructor() {
        AuthRequest authRequest = new AuthRequest("user1", "pass1");
        assertEquals("user1", authRequest.getUsername());
        assertEquals("pass1", authRequest.getPassword());
    }

    @Test
    public void testSettersAndGetters() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPass");

        assertEquals("testUser", authRequest.getUsername());
        assertEquals("testPass", authRequest.getPassword());
    }
}
