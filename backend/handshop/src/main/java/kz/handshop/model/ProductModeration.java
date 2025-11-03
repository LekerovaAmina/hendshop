package kz.handshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_moderations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductModeration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModerationAction action;

    @Column(columnDefinition = "TEXT")
    private String comment; // Обязателен для REJECT

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum ModerationAction {
        SUBMIT,     // Фрилансер отправил на модерацию
        APPROVE,    // Админ одобрил
        REJECT,     // Админ отклонил (нужен комментарий)
        BLOCK,      // Админ заблокировал после жалоб
        UNBLOCK,    // Админ разблокировал
        DELETE      // Админ удалил
    }

    // Helper методы
    public boolean requiresComment() {
        return action == ModerationAction.REJECT || action == ModerationAction.BLOCK;
    }
}