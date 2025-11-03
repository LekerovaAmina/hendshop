package kz.handshop.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReportRequest {
    @NotBlank(message = "Причина жалобы обязательна")
    @Pattern(
            regexp = "INAPPROPRIATE|FRAUD|POOR_QUALITY|COPYRIGHT|OTHER",
            message = "Недопустимая причина"
    )
    private String reason;

    private String comment;
}
