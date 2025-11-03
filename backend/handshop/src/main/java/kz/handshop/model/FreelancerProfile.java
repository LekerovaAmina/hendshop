package kz.handshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "freelancer_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Column(name = "shop_description", columnDefinition = "TEXT")
    private String shopDescription;

    @Column(name = "order_limit", nullable = false)
    private Integer orderLimit = 5;

    @Column(name = "rating")
    private Double rating = 0.0;

    @Column(name = "total_orders")
    private Integer totalOrders = 0;

    // Связь один-ко-многим с полками
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FreelancerShelf> shelves = new HashSet<>();

    // Связь один-ко-многим с товарами
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();

    // Связь один-ко-многим с заказами (как продавец)
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    // Helper методы
    public boolean hasAvailableSlots() {
        long activeOrders = orders.stream()
                .filter(order -> order.getStatus() != Order.OrderStatus.DELIVERED)
                .count();
        return activeOrders < orderLimit;
    }

    public long getActiveOrdersCount() {
        return orders.stream()
                .filter(order -> order.getStatus() != Order.OrderStatus.DELIVERED)
                .count();
    }
}