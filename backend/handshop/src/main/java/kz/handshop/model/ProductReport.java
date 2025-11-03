package kz.handshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_reports",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportReason reason;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_reviewed", nullable = false)
    private Boolean isReviewed = false;

    public enum ReportReason {
        INAPPROPRIATE,   // Неприемлемое содержание
        FRAUD,          // Мошенничество
        POOR_QUALITY,   // Низкое качество
        COPYRIGHT,      // Нарушение авторских прав
        OTHER           // Другое
    }

    // Helper методы
    public String getReasonDisplayName() {
        return switch (reason) {
            case INAPPROPRIATE -> "Неприемлемое содержание";
            case FRAUD -> "Мошенничество";
            case POOR_QUALITY -> "Низкое качество";
            case COPYRIGHT -> "Нарушение авторских прав";
            case OTHER -> "Другое";
        };
    }
}