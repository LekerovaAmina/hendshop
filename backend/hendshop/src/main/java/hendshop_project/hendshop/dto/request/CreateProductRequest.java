package kz.handshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    @NotBlank(message = "Название товара обязательно")
    @Size(max = 255)
    private String title;

    private String description;
    private String materials;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше 0")
    private BigDecimal price;

    @NotNull(message = "Время изготовления обязательно")
    @Min(value = 1, message = "Время изготовления минимум 1 день")
    private Integer productionTime;

    @NotBlank(message = "Тип доставки обязателен")
    @Pattern(regexp = "KAZPOST|YANDEX", message = "Тип доставки должен быть KAZPOST или YANDEX")
    private String deliveryType;
}
