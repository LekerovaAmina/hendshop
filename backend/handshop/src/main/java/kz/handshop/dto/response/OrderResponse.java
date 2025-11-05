package kz.handshop.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private BigDecimal price;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private SimpleUserResponse user;
    private SimpleUserResponse freelancer;
    private SimpleProductResponse product;
    private DeliveryAddressResponse deliveryAddress;

    public OrderResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SimpleUserResponse getUser() {
        return user;
    }

    public void setUser(SimpleUserResponse user) {
        this.user = user;
    }

    public SimpleUserResponse getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(SimpleUserResponse freelancer) {
        this.freelancer = freelancer;
    }

    public SimpleProductResponse getProduct() {
        return product;
    }

    public void setProduct(SimpleProductResponse product) {
        this.product = product;
    }

    public DeliveryAddressResponse getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddressResponse deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
