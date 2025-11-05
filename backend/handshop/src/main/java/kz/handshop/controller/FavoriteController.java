package kz.handshop.controller;

import kz.handshop.dto.response.MessageResponse;
import kz.handshop.dto.response.ProductResponse;
import kz.handshop.entity.User;
import kz.handshop.repository.UserRepository;
import kz.handshop.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{productId}")
    public ResponseEntity<MessageResponse> addToFavorites(@PathVariable Long productId,
                                                          Authentication authentication) {
        User user = getUserFromAuth(authentication);
        MessageResponse response = favoriteService.addToFavorites(productId, user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<MessageResponse> removeFromFavorites(@PathVariable Long productId,
                                                               Authentication authentication) {
        User user = getUserFromAuth(authentication);
        MessageResponse response = favoriteService.removeFromFavorites(productId, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getUserFavorites(Authentication authentication) {
        User user = getUserFromAuth(authentication);
        List<ProductResponse> favorites = favoriteService.getUserFavorites(user);
        return ResponseEntity.ok(favorites);
    }

    private User getUserFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}