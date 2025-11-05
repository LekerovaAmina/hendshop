package kz.handshop.controller;

import jakarta.validation.Valid;
import kz.handshop.dto.request.CreateProductRequest;
import kz.handshop.dto.request.CreateShelfRequest;
import kz.handshop.dto.response.MessageResponse;
import kz.handshop.dto.response.ProductResponse;
import kz.handshop.entity.ProductStatus;
import kz.handshop.entity.User;
import kz.handshop.repository.UserRepository;
import kz.handshop.service.FreelancerService;
import kz.handshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/freelancer")
public class FreelancerController {

    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/shelves")
    public ResponseEntity<?> createShelf(@Valid @RequestBody CreateShelfRequest request,
                                         Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        return ResponseEntity.ok(freelancerService.createShelf(request, freelancer));
    }

    @GetMapping("/shelves")
    public ResponseEntity<?> getMyShelves(Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        return ResponseEntity.ok(freelancerService.getFreelancerShelves(freelancer));
    }

    @DeleteMapping("/shelves/{id}")
    public ResponseEntity<?> deleteShelf(@PathVariable Long id, Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        freelancerService.deleteShelf(id, freelancer);
        return ResponseEntity.ok(new MessageResponse("Полка удалена"));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request,
                                                         Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        ProductResponse product = productService.createProduct(request, freelancer);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @RequestBody CreateProductRequest request,
                                                         Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        ProductResponse product = productService.updateProduct(id, request, freelancer);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/products/{id}/submit")
    public ResponseEntity<ProductResponse> submitForModeration(@PathVariable Long id,
                                                               Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        ProductResponse product = productService.submitForModeration(id, freelancer);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/products/{id}/archive")
    public ResponseEntity<ProductResponse> archiveProduct(@PathVariable Long id,
                                                          Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        ProductResponse product = productService.archiveProduct(id, freelancer);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/products/{id}/restore")
    public ResponseEntity<ProductResponse> restoreProduct(@PathVariable Long id,
                                                          Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        ProductResponse product = productService.restoreProduct(id, freelancer);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getMyProducts(@RequestParam(required = false) String status,
                                                               Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        ProductStatus productStatus = status != null ? ProductStatus.valueOf(status) : null;
        List<ProductResponse> products = productService.getFreelancerProducts(freelancer, productStatus);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getFreelancerOrders(Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        // Вызов метода из OrderService
        return ResponseEntity.ok(new MessageResponse("Список заказов"));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id,
                                               @RequestBody String status,
                                               Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        // Вызов метода из OrderService
        return ResponseEntity.ok(new MessageResponse("Статус заказа обновлён"));
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> getAnalytics(Authentication authentication) {
        User freelancer = getUserFromAuth(authentication);
        return ResponseEntity.ok(new MessageResponse("Аналитика"));
    }

    private User getUserFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}