package kz.handshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "ID товара обязателен")
    private Long productId;

    @NotNull(message = "ID адреса доставки обязателен")
    private Long deliveryAddressId;
}
