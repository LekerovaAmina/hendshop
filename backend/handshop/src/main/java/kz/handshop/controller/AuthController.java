package kz.handshop.controller;

import jakarta.validation.Valid;
import kz.handshop.dto.request.LoginRequest;
import kz.handshop.dto.request.RegisterFreelancerRequest;
import kz.handshop.dto.request.RegisterRequest;
import kz.handshop.dto.response.AuthResponse;
import kz.handshop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-freelancer")
    public ResponseEntity<AuthResponse> registerFreelancer(@Valid @RequestBody RegisterFreelancerRequest request) {
        AuthResponse response = authService.registerFreelancer(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}