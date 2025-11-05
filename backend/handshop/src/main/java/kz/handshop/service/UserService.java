package kz.handshop.service;

import kz.handshop.dto.request.UpdateProfileRequest;
import kz.handshop.dto.request.UpgradeToFreelancerRequest;
import kz.handshop.dto.response.FreelancerProfileResponse;
import kz.handshop.dto.response.UserResponse;
import kz.handshop.entity.*;
import kz.handshop.exception.ValidationException;
import kz.handshop.repository.FreelancerProfileRepository;
import kz.handshop.repository.SubscriptionRepository;
import kz.handshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FreelancerProfileRepository freelancerProfileRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public UserResponse getUserProfile(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setRole(user.getRole().name());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }

    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request, User user) {
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }

        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        user = userRepository.save(user);
        return getUserProfile(user);
    }

    @Transactional
    public FreelancerProfileResponse upgradeToFreelancer(UpgradeToFreelancerRequest request, User user) {
        // Проверка, что пользователь не является уже фрилансером
        if (user.getRole() == UserRole.FREELANCER || user.getRole() == UserRole.ADMIN) {
            throw new ValidationException("Вы уже являетесь фрилансером");
        }

        // Обновление роли
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

        // Формирование ответа
        FreelancerProfileResponse response = new FreelancerProfileResponse();
        response.setId(profile.getId());
        response.setShopName(profile.getShopName());
        response.setShopDescription(profile.getShopDescription());
        response.setOrderLimit(profile.getOrderLimit());
        response.setRating(profile.getRating());
        response.setTotalOrders(profile.getTotalOrders());

        return response;
    }
}