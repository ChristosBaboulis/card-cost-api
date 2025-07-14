package com.example.card_cost_api.resource;

import com.example.card_cost_api.auth.AuthRequest;
import com.example.card_cost_api.auth.AuthResponse;
import com.example.card_cost_api.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginResource {
    private final AuthService authService;

    public LoginResource(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest request) {
        ResponseEntity<Object> response = authService.authenticate(request.getUsername(), request.getPassword())
                .<ResponseEntity<Object>>map(token -> ResponseEntity.ok(new AuthResponse(token)))
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));

        return response;
    }
}
