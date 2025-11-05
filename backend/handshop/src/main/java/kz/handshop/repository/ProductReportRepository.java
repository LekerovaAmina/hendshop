package kz.handshop.repository;

import kz.handshop.entity.Product;
import kz.handshop.entity.ProductReport;
import kz.handshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReportRepository extends JpaRepository<ProductReport, Long> {
    List<ProductReport> findByProduct(Product product);
    List<ProductReport> findByProductId(Long productId);
    List<ProductReport> findByIsReviewedFalse();
    Optional<ProductReport> findByUserAndProduct(User user, Product product);
    Boolean existsByUserAndProduct(User user, Product product);
}
