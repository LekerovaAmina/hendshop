package kz.handshop.controller;

import kz.handshop.dto.response.FavoriteResponse;
import kz.handshop.model.Favorite;
import kz.handshop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * Получить избранное пользователя
     * GET /api/favorites
     */
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
            // TODO: Replace with @AuthenticationPrincipal after JWT setup
            @RequestParam Long userId) {

        List<Favorite> favorites = favoriteService.getUserFavorites(userId);

        List<FavoriteResponse> response = favorites.stream()
                .map(fav -> new FavoriteResponse(
                        fav.getId(),
                        fav.getProduct().getId(),
                        fav.getProduct().getTitle(),
                        fav.getProduct().getPrice(),
                        fav.getProduct().getPrimaryImageUrl(),
                        fav.getAddedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Добавить товар в избранное
     * POST /api/favorites/{productId}
     */
    @PostMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> addToFavorites(
            @PathVariable Long productId,
            @RequestParam Long userId) {

        Favorite favorite = favoriteService.addToFavorites(userId, productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Товар добавлен в избранное",
                "favoriteId", favorite.getId(),
                "productId", productId
        ));
    }

    /**
     * Удалить из избранного
     * DELETE /api/favorites/{productId}
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, String>> removeFromFavorites(
            @PathVariable Long productId,
            @RequestParam Long userId) {

        favoriteService.removeFromFavorites(userId, productId);

        return ResponseEntity.ok(Map.of(
                "message", "Товар удалён из избранного"
        ));
    }

    /**
     * Проверить, в избранном ли товар
     * GET /api/favorites/check/{productId}
     */
    @GetMapping("/check/{productId}")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(
            @PathVariable Long productId,
            @RequestParam Long userId) {

        boolean isFavorite = favoriteService.isFavorite(userId, productId);

        return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }
}