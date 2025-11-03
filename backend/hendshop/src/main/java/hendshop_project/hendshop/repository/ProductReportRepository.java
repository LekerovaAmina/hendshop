package kz.handshop.repository;

import kz.handshop.model.ProductReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductReportRepository extends JpaRepository<ProductReport, Long> {
    List<ProductReport> findByProductId(Long productId);
    List<ProductReport> findByIsReviewedFalse();
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT pr FROM ProductReport pr WHERE pr.product.status = 'BLOCKED' AND pr.isReviewed = false")
    List<ProductReport> findUnreviewedForBlockedProducts();

    @Query("SELECT COUNT(pr) FROM ProductReport pr WHERE pr.product.id = :productId")
    Long countByProductId(@Param("productId") Long productId);
}
