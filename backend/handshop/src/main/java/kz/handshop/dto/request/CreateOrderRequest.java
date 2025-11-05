package kz.handshop.dto.request;

import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {

    @NotNull(message = "ID товара обязателен")
    private Long productId;

    @NotNull(message = "ID адреса доставки обязателен")
    private Long deliveryAddressId;

    public CreateOrderRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }
}