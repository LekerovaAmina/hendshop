package kz.handshop.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "category_requests")
public class CategoryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "requested_category_name", nullable = false, length = 150)
    private String requestedCategoryName;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_reviewed")
    private Boolean isReviewed = false;

    // Constructors
    public CategoryRequest() {
    }

    public CategoryRequest(User user, String requestedCategoryName) {
        this.user = user;
        this.requestedCategoryName = requestedCategoryName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRequestedCategoryName() {
        return requestedCategoryName;
    }

    public void setRequestedCategoryName(String requestedCategoryName) {
        this.requestedCategoryName = requestedCategoryName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsReviewed() {
        return isReviewed;
    }

    public void setIsReviewed(Boolean isReviewed) {
        this.isReviewed = isReviewed;
    }
}