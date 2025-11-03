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
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;

    /**
     * Создать заказ
     */
    @Transactional
    public Order createOrder(Long userId, Long productId, Long deliveryAddressId) {
        // Проверка пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        // Проверка товара
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден"));

        // Товар должен быть опубликован
        if (product.getStatus() != Product.ProductStatus.PUBLISHED) {
            throw new InvalidStatusException("Товар недоступен для заказа");
        }

        // Проверка лимита заказов фрилансера
        FreelancerProfile freelancerProfile = freelancerProfileRepository
                .findByUserId(product.getFreelancer().getId())
                .orElseThrow(() -> new UserNotFoundException("Фрилансер не найден"));

        if (!freelancerProfile.hasAvailableSlots()) {
            throw new OrderLimitExceededException(
                    "У фрилансера нет свободных слотов для заказов. " +
                            "Добавьте товар в избранное и проверьте позже."
            );
        }

        // Проверка адреса доставки
        DeliveryAddress address = deliveryAddressRepository.findById(deliveryAddressId)
                .orElseThrow(() -> new ValidationException("Адрес доставки не найден"));

        if (!address.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Это не ваш адрес доставки");
        }

        // Создание заказа
        Order order = new Order();
        order.setUser(user);
        order.setFreelancer(product.getFreelancer());
        order.setProduct(product);
        order.setDeliveryAddress(address);
        order.setPrice(product.getPrice()); // Цена на момент заказа
        order.setStatus(Order.OrderStatus.NEW);

        order = orderRepository.save(order);

        // Увеличение счётчика заказов у фрилансера
        freelancerProfile.setTotalOrders(freelancerProfile.getTotalOrders() + 1);
        freelancerProfileRepository.save(freelancerProfile);

        return order;
    }

    /**
     * Получить заказы пользователя
     */
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * Получить заказы фрилансера
     */
    public List<Order> getFreelancerOrders(Long freelancerId) {
        return orderRepository.findByFreelancerId(freelancerId);
    }

    /**
     * Получить активные заказы фрилансера
     */
    public List<Order> getActiveFreelancerOrders(Long freelancerId) {
        return orderRepository.findActiveByFreelancerId(freelancerId);
    }

    /**
     * Изменить статус заказа (для фрилансера)
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, Long freelancerId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Заказ не найден"));

        // Проверка прав
        if (!order.getFreelancer().getId().equals(freelancerId)) {
            throw new ForbiddenException("Вы не можете изменить этот заказ");
        }

        // Проверка логики смены статусов
        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new InvalidStatusException(
                    "Недопустимый переход статуса: " + order.getStatus() + " → " + newStatus
            );
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    /**
     * Подтвердить доставку (для пользователя)
     */
    @Transactional
    public Order confirmDelivery(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Заказ не найден"));

        // Проверка прав
        if (!order.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Это не ваш заказ");
        }

        // Можно подтвердить только если статус SHIPPED
        if (order.getStatus() != Order.OrderStatus.SHIPPED) {
            throw new InvalidStatusException("Заказ ещё не отправлен");
        }

        order.setStatus(Order.OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }

    /**
     * Проверка допустимости смены статуса
     */
    private boolean isValidStatusTransition(Order.OrderStatus current, Order.OrderStatus next) {
        return switch (current) {
            case NEW -> next == Order.OrderStatus.IN_PROGRESS;
            case IN_PROGRESS -> next == Order.OrderStatus.READY;
            case READY -> next == Order.OrderStatus.SHIPPED;
            case SHIPPED -> next == Order.OrderStatus.DELIVERED;
            case DELIVERED -> false; // Финальный статус
        };
    }
}