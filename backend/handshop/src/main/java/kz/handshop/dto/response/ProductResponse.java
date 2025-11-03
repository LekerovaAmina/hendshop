package kz.handshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String title;
    private String description;
    private String materials;
    private BigDecimal price;
    private Integer productionTime;
    private String deliveryType;
    private String status;
    private String primaryImageUrl;
    private Integer viewsCount;
    private Double averageRating;
    private Integer reviewsCount;
    private Long freelancerId;
    private String freelancerName;
}
