package kz.handshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerProfileResponse {
    private Long id;
    private String shopName;
    private String shopDescription;
    private Integer orderLimit;
    private Double rating;
    private Integer totalOrders;
    private Long activeOrdersCount;
}
