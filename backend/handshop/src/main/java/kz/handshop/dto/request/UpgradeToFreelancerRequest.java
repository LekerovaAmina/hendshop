package kz.handshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpgradeToFreelancerRequest {
    @NotBlank(message = "Название магазина обязательно")
    private String shopName;

    private String shopDescription;
}
