package kz.handshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private User freelancer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private DeliveryAddress deliveryAddress;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Цена на момент заказа

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.NEW;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum OrderStatus {
        NEW,          // Новый
        IN_PROGRESS,  // В процессе
        READY,        // Готов
        SHIPPED,      // Передан в доставку
        DELIVERED     // Доставлен
    }

    // Helper методы
    public boolean isActive() {
        return status != OrderStatus.DELIVERED;
    }

    public boolean canChangeStatus() {
        return status != OrderStatus.DELIVERED;
    }

    public void nextStatus() {
        switch (status) {
            case NEW -> status = OrderStatus.IN_PROGRESS;
            case IN_PROGRESS -> status = OrderStatus.READY;
            case READY -> status = OrderStatus.SHIPPED;
            case SHIPPED -> status = OrderStatus.DELIVERED;
        }
    }
}
