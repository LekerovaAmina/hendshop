package kz.handshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private Long id;
    private Long productId;
    private String productTitle;
    private Long userId;
    private String username;
    private String reason;
    private String comment;
    private LocalDateTime createdAt;
    private Boolean isReviewed;
}
