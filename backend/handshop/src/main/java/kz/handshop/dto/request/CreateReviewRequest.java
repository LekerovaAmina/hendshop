package kz.handshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    @NotNull(message = "ID заказа обязателен")
    private Long orderId;

    @NotNull(message = "Рейтинг обязателен")
    @Min(value = 1, message = "Рейтинг минимум 1")
    @Max(value = 5, message = "Рейтинг максимум 5")
    private Integer rating;

    private String comment;
}
