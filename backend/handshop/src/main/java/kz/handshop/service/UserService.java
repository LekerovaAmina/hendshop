package kz.handshop.service;

import kz.handshop.exception.*;
import kz.handshop.model.*;
import kz.handshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final SubscriptionRepository subscriptionRepository;

    /**
     * Получить пользователя по ID
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    /**
     * Получить пользователя по email
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    /**
     * Обновить профиль пользователя
     */
    @Transactional
    public User updateProfile(Long userId, String username, String avatarUrl) {
        User user = getUserById(userId);

        if (username != null && !username.trim().isEmpty()) {
            user.setUsername(username);
        }

        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            user.setAvatarUrl(avatarUrl);
        }

        return userRepository.save(user);
    }

    /**
     * Повысить пользователя до фрилансера
     */
    @Transactional
    public User upgradeToFreelancer(Long userId, String shopName, String shopDescription) {
        User user = getUserById(userId);

        // Проверка: пользователь уже фрилансер?
        if (user.isFreelancer()) {
            throw new ValidationException("Пользователь уже является фрилансером");
        }

        // Смена роли
        user.setRole(User.Role.FREELANCER);
        user = userRepository.save(user);

        // Создание профиля фрилансера
        FreelancerProfile profile = new FreelancerProfile();
        profile.setUser(user);
        profile.setShopName(shopName);
        profile.setShopDescription(shopDescription);
        profile.setOrderLimit(5);
        profile.setRating(0.0);
        profile.setTotalOrders(0);
        freelancerProfileRepository.save(profile);

        // Активация подписки
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setIsActive(true);
        subscription.setStartedAt(LocalDateTime.now());
        subscription.setExpiresAt(LocalDateTime.now().plusMonths(1));
        subscriptionRepository.save(subscription);

        return user;
    }

    /**
     * Получить профиль фрилансера
     */
    public FreelancerProfile getFreelancerProfile(Long userId) {
        return freelancerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Профиль фрилансера не найден"));
    }

    /**
     * Обновить профиль фрилансера
     */
    @Transactional
    public FreelancerProfile updateFreelancerProfile(Long userId, String shopName,
                                                     String shopDescription, Integer orderLimit) {
        FreelancerProfile profile = getFreelancerProfile(userId);

        if (shopName != null && !shopName.trim().isEmpty()) {
            profile.setShopName(shopName);
        }

        if (shopDescription != null) {
            profile.setShopDescription(shopDescription);
        }

        if (orderLimit != null && orderLimit >= 1 && orderLimit <= 50) {
            profile.setOrderLimit(orderLimit);
        }

        return freelancerProfileRepository.save(profile);
    }
}