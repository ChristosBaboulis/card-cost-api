package com.example.card_cost_api.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void testNoArgsConstructor() {
        User user = new User();
        Assertions.assertNotNull(user);
    }

    @Test
    public void testAllArgsConstructor() {
        User user = new User("testuser", "testpass", "ADMIN");

        Assertions.assertEquals("testuser", user.getUsername());
        Assertions.assertEquals("testpass", user.getPassword());
        Assertions.assertEquals("ADMIN", user.getRole());
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("pass1");

        Assertions.assertEquals("user1", user.getUsername());
        Assertions.assertEquals("pass1", user.getPassword());
    }

    @Test
    public void testIdDefaultValue() {
        User user = new User();
        // id should be 0 before persistence
        Assertions.assertNull(user.getId());
    }
}
