package kz.handshop.service;

import kz.handshop.exception.*;
import kz.handshop.model.*;
import kz.handshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Добавить товар в избранное
     */
    @Transactional
    public Favorite addToFavorites(Long userId, Long productId) {
        // Проверка пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        // Проверка товара
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        // Товар должен быть опубликован
        if (product.getStatus() != Product.ProductStatus.PUBLISHED) {
            throw new InvalidStatusException("Можно добавлять только опубликованные товары");
        }

        // Проверка: товар уже в избранном?
        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new ValidationException("Товар уже в избранном");
        }

        // Создание избранного
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);

        return favoriteRepository.save(favorite);
    }

    /**
     * Удалить из избранного
     */
    @Transactional
    public void removeFromFavorites(Long userId, Long productId) {
        if (!favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new ValidationException("Товар не найден в избранном");
        }

        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }

    /**
     * Получить избранное пользователя
     */
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    /**
     * Проверить, в избранном ли товар
     */
    public boolean isFavorite(Long userId, Long productId) {
        return favoriteRepository.existsByUserIdAndProductId(userId, productId);
    }
}