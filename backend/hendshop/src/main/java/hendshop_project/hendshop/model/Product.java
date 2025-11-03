package kz.handshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private User freelancer;

    @ManyToOne
    @JoinColumn(name = "shelf_id")
    private FreelancerShelf shelf;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String materials;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "production_time")
    private Integer productionTime; // в днях

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DeliveryType deliveryType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.DRAFT;

    @Column(name = "views_count")
    private Integer viewsCount = 0;

    @Column(name = "reports_count")
    private Integer reportsCount = 0; // Счётчик жалоб

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Связь один-ко-многим с изображениями
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<ProductImage> images = new ArrayList<>();

    // Связь один-ко-многим с модерациями
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductModeration> moderations = new HashSet<>();

    // Связь один-ко-многим с жалобами
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductReport> reports = new HashSet<>();

    // Связь один-ко-многим с избранным
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Favorite> favorites = new HashSet<>();

    // Связь один-ко-многим с заказами
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    // Связь один-ко-многим с отзывами
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductReview> reviews = new HashSet<>();

    public enum ProductStatus {
        DRAFT,              // Черновик
        MODERATION,         // На модерации
        EDIT_MODERATION,    // Отправлен на доработку
        PUBLISHED,          // Опубликован
        ARCHIVED,           // В архиве
        BLOCKED,            // Заблокирован (5+ жалоб)
        DELETED             // Удалён админом
    }

    public enum DeliveryType {
        KAZPOST,
        YANDEX
    }

    // ===============================
    // Helper методы
    // ===============================

    public void incrementViews() {
        this.viewsCount++;
    }

    public String getPrimaryImageUrl() {
        return images.stream()
                .filter(ProductImage::getIsPrimary)
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(null);
    }

    public boolean isPublished() {
        return status == ProductStatus.PUBLISHED;
    }

    public boolean isBlocked() {
        return status == ProductStatus.BLOCKED;
    }

    public boolean isDeleted() {
        return status == ProductStatus.DELETED;
    }

    // Проверка заполненности всех обязательных полей
    public boolean isComplete() {
        return title != null && !title.trim().isEmpty()
                && description != null && !description.trim().isEmpty()
                && materials != null && !materials.trim().isEmpty()
                && price != null && price.compareTo(BigDecimal.ZERO) > 0
                && productionTime != null && productionTime > 0
                && deliveryType != null
                && !images.isEmpty(); // Хотя бы одно фото
    }

    // Можно ли отправить на модерацию
    public boolean canSubmitForModeration() {
        return (status == ProductStatus.DRAFT || status == ProductStatus.EDIT_MODERATION)
                && isComplete();
    }

    // Видим ли товар обычным пользователям
    public boolean isVisibleToUsers() {
        return status == ProductStatus.PUBLISHED;
    }

    // Автоблокировка при достижении 5 жалоб
    public void checkAutoBlock() {
        if (reportsCount >= 5 && status == ProductStatus.PUBLISHED) {
            this.status = ProductStatus.BLOCKED;
        }
    }

    // Получить средний рейтинг товара
    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(ProductReview::getRating)
                .average()
                .orElse(0.0);
    }

    // Получить количество отзывов
    public int getReviewsCount() {
        return reviews != null ? reviews.size() : 0;
    }
}