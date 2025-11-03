package kz.handshop.controller;

import kz.handshop.dto.request.CreateProductRequest;
import kz.handshop.dto.request.UpdateOrderStatusRequest;
import kz.handshop.dto.response.OrderResponse;
import kz.handshop.dto.response.ProductResponse;
import kz.handshop.model.Order;
import kz.handshop.model.Product;
import kz.handshop.service.OrderService;
import kz.handshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/freelancer")
@RequiredArgsConstructor
public class FreelancerController {

    private final ProductService productService;
    private final OrderService orderService;

    // ==================== ТОВАРЫ ====================

    /**
     * Получить свои товары
     * GET /api/freelancer/products
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getMyProducts(
            @RequestParam Long freelancerId,
            @RequestParam(required = false) String status) {

        List<Product> products;

        if (status != null) {
            Product.ProductStatus productStatus = Product.ProductStatus.valueOf(status);
            products = productService.getFreelancerProducts(freelancerId).stream()
                    .filter(p -> p.getStatus() == productStatus)
                    .collect(Collectors.toList());
        } else {
            products = productService.getFreelancerProducts(freelancerId);
        }

        List<ProductResponse> response = products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Создать товар (черновик)
     * POST /api/freelancer/products
     */
    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            @RequestParam Long freelancerId) {

        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setMaterials(request.getMaterials());
        product.setPrice(request.getPrice());
        product.setProductionTime(request.getProductionTime());
        product.setDeliveryType(Product.DeliveryType.valueOf(request.getDeliveryType()));

        Product created = productService.createProduct(product, freelancerId);

        return ResponseEntity.status(HttpStatus.CREATED).body(toProductResponse(created));
    }

    /**
     * Обновить товар
     * PUT /api/freelancer/products/{id}
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequest request,
            @RequestParam Long freelancerId) {

        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setMaterials(request.getMaterials());
        product.setPrice(request.getPrice());
        product.setProductionTime(request.getProductionTime());
        product.setDeliveryType(Product.DeliveryType.valueOf(request.getDeliveryType()));

        Product updated = productService.updateProduct(id, product, freelancerId);

        return ResponseEntity.ok(toProductResponse(updated));
    }

    /**
     * Отправить на модерацию
     * PATCH /api/freelancer/products/{id}/submit
     */
    @PatchMapping("/products/{id}/submit")
    public ResponseEntity<ProductResponse> submitForModeration(
            @PathVariable Long id,
            @RequestParam Long freelancerId) {

        Product product = productService.submitForModeration(id, freelancerId);

        return ResponseEntity.ok(toProductResponse(product));
    }

    /**
     * Архивировать товар
     * PATCH /api/freelancer/products/{id}/archive
     */
    @PatchMapping("/products/{id}/archive")
    public ResponseEntity<ProductResponse> archiveProduct(
            @PathVariable Long id,
            @RequestParam Long freelancerId) {

        Product product = productService.archiveProduct(id, freelancerId);

        return ResponseEntity.ok(toProductResponse(product));
    }

    /**
     * Восстановить из архива
     * PATCH /api/freelancer/products/{id}/restore
     */
    @PatchMapping("/products/{id}/restore")
    public ResponseEntity<ProductResponse> restoreProduct(
            @PathVariable Long id,
            @RequestParam Long freelancerId) {

        Product product = productService.restoreFromArchive(id, freelancerId);

        return ResponseEntity.ok(toProductResponse(product));
    }

    // ==================== ЗАКАЗЫ ====================

    /**
     * Получить свои заказы
     * GET /api/freelancer/orders
     */
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@RequestParam Long freelancerId) {
        List<Order> orders = orderService.getFreelancerOrders(freelancerId);

        List<OrderResponse> response = orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Изменить статус заказа
     * PATCH /api/freelancer/orders/{id}/status
     */
    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request,
            @RequestParam Long freelancerId) {

        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(request.getStatus());
        Order order = orderService.updateOrderStatus(id, freelancerId, newStatus);

        return ResponseEntity.ok(toOrderResponse(order));
    }

    // ==================== HELPER METHODS ====================

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

    private OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProduct().getId(),
                order.getProduct().getTitle(),
                order.getProduct().getPrimaryImageUrl(),
                order.getPrice(),
                order.getStatus().name(),
                order.getDeliveryAddress().getFullAddress(),
                order.getFreelancer().getId(),
                order.getFreelancer().getUsername(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}