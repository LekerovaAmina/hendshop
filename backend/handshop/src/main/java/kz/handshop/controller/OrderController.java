package kz.handshop.controller;

import kz.handshop.dto.request.CreateOrderRequest;
import kz.handshop.dto.response.OrderResponse;
import kz.handshop.model.Order;
import kz.handshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Создать заказ
     * POST /api/orders
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestParam Long userId) {

        Order order = orderService.createOrder(
                userId,
                request.getProductId(),
                request.getDeliveryAddressId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(toOrderResponse(order));
    }

    /**
     * Получить заказы пользователя
     * GET /api/orders/my
     */
    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@RequestParam Long userId) {
        List<Order> orders = orderService.getUserOrders(userId);

        List<OrderResponse> response = orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Получить заказ по ID
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id,
            @RequestParam Long userId) {

        Order order = orderService.getUserOrders(userId).stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElseThrow();

        return ResponseEntity.ok(toOrderResponse(order));
    }

    /**
     * Подтвердить доставку
     * PATCH /api/orders/{id}/delivered
     */
    @PatchMapping("/{id}/delivered")
    public ResponseEntity<OrderResponse> confirmDelivery(
            @PathVariable Long id,
            @RequestParam Long userId) {

        Order order = orderService.confirmDelivery(id, userId);

        return ResponseEntity.ok(toOrderResponse(order));
    }

    // Helper method
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