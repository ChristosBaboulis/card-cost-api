package com.example.card_cost_api.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class AuthResponseTest {
    @Test
    public void testConstructorAndGetters() {
        String token = "sample-jwt-token";
        AuthResponse response = new AuthResponse(token);

        assertEquals(token, response.getToken(), "Token should match the one passed to constructor");
    }

    @Test
    public void testSetters() {
        AuthResponse response = new AuthResponse();
        String token = "new-token";
        response.setToken(token);

        assertEquals(token, response.getToken(), "Token should be updated correctly via setter");
    }
}
