package kz.handshop.service;

import kz.handshop.dto.request.LoginRequest;
import kz.handshop.dto.request.RegisterFreelancerRequest;
import kz.handshop.dto.request.RegisterRequest;
import kz.handshop.dto.response.AuthResponse;
import kz.handshop.dto.response.FreelancerProfileResponse;
import kz.handshop.entity.*;
import kz.handshop.exception.EmailAlreadyExistsException;
import kz.handshop.repository.FreelancerProfileRepository;
import kz.handshop.repository.SubscriptionRepository;
import kz.handshop.repository.UserRepository;
import kz.handshop.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FreelancerProfileRepository freelancerProfileRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse registerUser(RegisterRequest request) {
        // Проверка на существование email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email уже используется");
        }

        // Создание пользователя
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);

        user = userRepository.save(user);

        // Генерация токена
        String token = jwtTokenProvider.generateTokenFromEmail(user.getEmail());

        // Формирование ответа
        AuthResponse response = new AuthResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setToken(token);

        return response;
    }

    @Transactional
    public AuthResponse registerFreelancer(RegisterFreelancerRequest request) {
        // Проверка на существование email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email уже используется");
        }

        // Создание пользователя
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.FREELANCER);

        user = userRepository.save(user);

        // Создание профиля фрилансера
        FreelancerProfile profile = new FreelancerProfile();
        profile.setUser(user);
        profile.setShopName(request.getShopName());
        profile.setShopDescription(request.getShopDescription());
        profile.setOrderLimit(5);

        profile = freelancerProfileRepository.save(profile);

        // Создание активной подписки на 30 дней
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setIsActive(true);
        subscription.setStartedAt(LocalDateTime.now());
        subscription.setExpiresAt(LocalDateTime.now().plusDays(30));

        subscriptionRepository.save(subscription);

        // Генерация токена
        String token = jwtTokenProvider.generateTokenFromEmail(user.getEmail());

        // Формирование ответа
        AuthResponse response = new AuthResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setToken(token);

        // Добавление информации о профиле фрилансера
        FreelancerProfileResponse profileResponse = new FreelancerProfileResponse();
        profileResponse.setId(profile.getId());
        profileResponse.setShopName(profile.getShopName());
        profileResponse.setShopDescription(profile.getShopDescription());
        profileResponse.setOrderLimit(profile.getOrderLimit());
        profileResponse.setRating(profile.getRating());
        profileResponse.setTotalOrders(profile.getTotalOrders());

        response.setFreelancerProfile(profileResponse);

        return response;
    }

    public AuthResponse login(LoginRequest request) {
        // Аутентификация
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Генерация токена
        String token = jwtTokenProvider.generateToken(authentication);

        // Получение пользователя
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Формирование ответа
        AuthResponse response = new AuthResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setToken(token);

        // Если фрилансер, добавить профиль
        if (user.getRole() == UserRole.FREELANCER || user.getRole() == UserRole.ADMIN) {
            freelancerProfileRepository.findByUser(user).ifPresent(profile -> {
                FreelancerProfileResponse profileResponse = new FreelancerProfileResponse();
                profileResponse.setId(profile.getId());
                profileResponse.setShopName(profile.getShopName());
                profileResponse.setShopDescription(profile.getShopDescription());
                profileResponse.setOrderLimit(profile.getOrderLimit());
                profileResponse.setRating(profile.getRating());
                profileResponse.setTotalOrders(profile.getTotalOrders());

                response.setFreelancerProfile(profileResponse);
            });
        }

        return response;
    }
}