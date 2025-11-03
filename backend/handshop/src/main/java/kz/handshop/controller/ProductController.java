package kz.handshop.controller;

import kz.handshop.dto.response.ProductResponse;
import kz.handshop.model.Product;
import kz.handshop.repository.ProductRepository;
import kz.handshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    /**
     * Получить все опубликованные товары
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search) {

        List<Product> products;

        if (search != null && !search.isEmpty()) {
            products = productRepository.searchPublished(search);
        } else if (categoryId != null) {
            products = productRepository.findByFreelancerAndCategory(null, categoryId);
        } else {
            products = productService.getAllPublishedProducts();
        }

        List<ProductResponse> response = products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Получить товар по ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);

        // Увеличить счётчик просмотров
        productService.incrementViews(id);

        return ResponseEntity.ok(toProductResponse(product));
    }

    /**
     * Получить отзывы товара
     * GET /api/products/{id}/reviews
     */
    @GetMapping("/{id}/reviews")
    public ResponseEntity<?> getProductReviews(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        // TODO: Return reviews DTO
        return ResponseEntity.ok(product.getReviews());
    }

    // Helper method для преобразования в DTO
    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getMaterials(),
                product.getPrice(),
                product.getProductionTime(),
                product.getDeliveryType().name(),
                product.getStatus().name(),
                product.getPrimaryImageUrl(),
                product.getViewsCount(),
                product.getAverageRating(),
                product.getReviewsCount(),
                product.getFreelancer().getId(),
                product.getFreelancer().getUsername()
        );
    }
}