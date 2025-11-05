package kz.handshop.controller;

import jakarta.validation.Valid;
import kz.handshop.dto.request.CreateCategoryRequest;
import kz.handshop.dto.request.RejectProductRequest;
import kz.handshop.dto.response.CategoryResponse;
import kz.handshop.dto.response.MessageResponse;
import kz.handshop.dto.response.ProductResponse;
import kz.handshop.entity.ProductReport;
import kz.handshop.entity.User;
import kz.handshop.repository.UserRepository;
import kz.handshop.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepository userRepository;

    // 1111111111111111111111111111111111111 Категории

    @PostMapping("/global-categories")
    public ResponseEntity<CategoryResponse> createGlobalCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse category = adminService.createGlobalCategory(request);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/global-categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = adminService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // 222222222222222222222222222222222222222 Модерация товаров

    @GetMapping("/products/moderation")
    public ResponseEntity<List<ProductResponse>> getProductsForModeration() {
        List<ProductResponse> products = adminService.getProductsForModeration();
        return ResponseEntity.ok(products);
    }

    @PatchMapping("/products/{id}/approve")
    public ResponseEntity<ProductResponse> approveProduct(@PathVariable Long id,
                                                          Authentication authentication) {
        User admin = getUserFromAuth(authentication);
        ProductResponse product = adminService.approveProduct(id, admin);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/products/{id}/reject")
    public ResponseEntity<ProductResponse> rejectProduct(@PathVariable Long id,
                                                         @Valid @RequestBody RejectProductRequest request,
                                                         Authentication authentication) {
        User admin = getUserFromAuth(authentication);
        ProductResponse product = adminService.rejectProduct(id, request.getComment(), admin);
        return ResponseEntity.ok(product);
    }

    // 3333333333333333333333333333333333333333333 Жалобы

    @GetMapping("/reports")
    public ResponseEntity<List<ProductReport>> getUnreviewedReports() {
        List<ProductReport> reports = adminService.getUnreviewedReports();
        return ResponseEntity.ok(reports);
    }

    @PatchMapping("/reports/{productId}/reject")
    public ResponseEntity<MessageResponse> rejectReports(@PathVariable Long productId) {
        MessageResponse response = adminService.rejectReports(productId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/reports/{productId}/accept")
    public ResponseEntity<MessageResponse> acceptReports(@PathVariable Long productId,
                                                         Authentication authentication) {
        User admin = getUserFromAuth(authentication);
        MessageResponse response = adminService.acceptReports(productId, admin);
        return ResponseEntity.ok(response);
    }

    // Вспомогательный метод
    private User getUserFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}