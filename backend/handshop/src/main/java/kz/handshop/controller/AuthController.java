package kz.handshop.controller;

import kz.handshop.dto.request.LoginRequest;
import kz.handshop.dto.request.RegisterFreelancerRequest;
import kz.handshop.dto.request.RegisterRequest;
import kz.handshop.dto.response.AuthResponse;
import kz.handshop.model.User;
import kz.handshop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    // TODO: Add JwtTokenProvider after creating it
    // private final JwtTokenProvider jwtTokenProvider;

    /**
     * Регистрация обычного пользователя
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        User user = authService.registerUser(
                request.getEmail(),
                request.getUsername(),
                request.getPassword()
        );

        // TODO: Generate JWT token
        String token = "temp_token_" + user.getId(); // Заглушка

        AuthResponse response = new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name(),
                token
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Вход пользователя
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request.getEmail(), request.getPassword());

        // TODO: Generate JWT token
        String token = "temp_token_" + user.getId(); // Заглушка

        AuthResponse response = new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name(),
                token
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Регистрация фрилансера
     * POST /api/auth/register-freelancer
     */
    @PostMapping("/register-freelancer")
    public ResponseEntity<AuthResponse> registerFreelancer(
            @Valid @RequestBody RegisterFreelancerRequest request) {

        User user = authService.registerFreelancer(
                request.getEmail(),
                request.getUsername(),
                request.getPassword(),
                request.getShopName(),
                request.getShopDescription()
        );

        // TODO: Generate JWT token
        String token = "temp_token_" + user.getId(); // Заглушка

        AuthResponse response = new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name(),
                token
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}