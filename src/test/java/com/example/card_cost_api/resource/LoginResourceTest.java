//package com.example.card_cost_api.resource;
//
//import com.example.card_cost_api.auth.AuthRequest;
//import com.example.card_cost_api.auth.AuthResponse;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//
//@SpringBootTest
//@Sql("/import.sql")
//public class LoginResourceTest {
//
//    @Test
//    public void testSuccessfulLoginReturnsToken() {
//        AuthRequest request = new AuthRequest("csupport1", "securePass123");
//
//        AuthResponse response = given()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when()
//                .post("/auth/login")
//                .then()
//                .statusCode(200)
//                .extract()
//                .as(AuthResponse.class);
//
//        Assertions.assertNotNull(response);
//        Assertions.assertNotNull(response.getToken());
//        Assertions.assertFalse(response.getToken().isBlank());
//    }
//
//    @Test
//    public void testInvalidPasswordReturns401() {
//        AuthRequest request = new AuthRequest("csupport1", "wrongPassword");
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when()
//                .post("/auth/login")
//                .then()
//                .statusCode(401);
//    }
//
//    @Test
//    public void testUnknownUsernameReturns401() {
//        AuthRequest request = new AuthRequest("unknownUser", "password");
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(request)
//                .when()
//                .post("/auth/login")
//                .then()
//                .statusCode(401);
//    }
//
//}
