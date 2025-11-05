package kz.handshop.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "freelancer_shelves")
public class FreelancerShelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private User freelancer;

    @ManyToOne
    @JoinColumn(name = "global_category_id")
    private GlobalCategory globalCategory;

    @Column(name = "shelf_name", nullable = false, length = 150)
    private String shelfName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public FreelancerShelf() {
    }

    public FreelancerShelf(User freelancer, GlobalCategory globalCategory, String shelfName) {
        this.freelancer = freelancer;
        this.globalCategory = globalCategory;
        this.shelfName = shelfName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(User freelancer) {
        this.freelancer = freelancer;
    }

    public GlobalCategory getGlobalCategory() {
        return globalCategory;
    }

    public void setGlobalCategory(GlobalCategory globalCategory) {
        this.globalCategory = globalCategory;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}