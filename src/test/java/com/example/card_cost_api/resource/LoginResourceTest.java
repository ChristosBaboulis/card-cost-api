package com.example.card_cost_api.resource;

import com.example.card_cost_api.auth.AuthRequest;
import com.example.card_cost_api.auth.AuthResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/import.sql")
public class LoginResourceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void testSuccessfulLoginReturnsToken() {
        AuthRequest request = new AuthRequest("admin1", "adminPass");

        AuthResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .as(AuthResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getToken());
        Assertions.assertFalse(response.getToken().isBlank());
    }

    @Test
    public void testInvalidPasswordReturns401() {
        AuthRequest request = new AuthRequest("user1", "wrongPassword");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void testUnknownUsernameReturns401() {
        AuthRequest request = new AuthRequest("unknownUser", "password");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }
}
