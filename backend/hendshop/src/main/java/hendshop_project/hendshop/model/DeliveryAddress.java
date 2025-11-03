package kz.handshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "delivery_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String building;

    @Column
    private String apartment;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Связь один-ко-многим с заказами
    @OneToMany(mappedBy = "deliveryAddress", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    // Helper методы
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        address.append(city).append(", ");
        address.append(street).append(", ");
        address.append(building);
        if (apartment != null && !apartment.isEmpty()) {
            address.append(", кв. ").append(apartment);
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            address.append(", ").append(postalCode);
        }
        return address.toString();
    }
}