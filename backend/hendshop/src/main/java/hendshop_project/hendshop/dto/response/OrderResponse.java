package kz.handshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long productId;
    private String productTitle;
    private String productImageUrl;
    private BigDecimal price;
    private String status;
    private String deliveryAddress;
    private Long freelancerId;
    private String freelancerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
