package kz.handshop.service;

import kz.handshop.exception.*;
import kz.handshop.model.*;
import kz.handshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Регистрация обычного пользователя
     */
    @Transactional
    public User registerUser(String email, String username, String password) {
        // Проверка на существование email
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email уже зарегистрирован");
        }

        // Создание пользователя
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(User.Role.USER);

        return userRepository.save(user);
    }

    /**
     * Регистрация фрилансера
     */
    @Transactional
    public User registerFreelancer(String email, String username, String password,
                                   String shopName, String shopDescription) {
        // Проверка на существование email
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email уже зарегистрирован");
        }

        // Создание пользователя с ролью FREELANCER
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(User.Role.FREELANCER);
        user = userRepository.save(user);

        // Создание профиля фрилансера
        FreelancerProfile profile = new FreelancerProfile();
        profile.setUser(user);
        profile.setShopName(shopName);
        profile.setShopDescription(shopDescription);
        profile.setOrderLimit(5); // По умолчанию 5 заказов
        profile.setRating(0.0);
        profile.setTotalOrders(0);
        freelancerProfileRepository.save(profile);

        // Создание подписки (активируется после оплаты)
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setIsActive(true); // Для MVP сразу активна
        subscription.setStartedAt(LocalDateTime.now());
        subscription.setExpiresAt(LocalDateTime.now().plusMonths(1));
        subscriptionRepository.save(subscription);

        return user;
    }

    /**
     * Аутентификация пользователя
     */
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Неверный email или пароль"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ValidationException("Неверный email или пароль");
        }

        return user;
    }
}