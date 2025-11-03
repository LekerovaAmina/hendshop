package kz.handshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {
    @NotBlank(message = "Статус обязателен")
    @Pattern(
            regexp = "NEW|IN_PROGRESS|READY|SHIPPED|DELIVERED",
            message = "Недопустимый статус"
    )
    private String status;
}
