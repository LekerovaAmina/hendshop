package kz.handshop.repository;

import kz.handshop.entity.Product;
import kz.handshop.entity.ProductReview;
import kz.handshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByProduct(Product product);
    List<ProductReview> findByProductId(Long productId);
    Optional<ProductReview> findByUserAndProduct(User user, Product product);
    Boolean existsByUserAndProduct(User user, Product product);

    @Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.product.id = :productId")
    BigDecimal getAverageRatingByProductId(@Param("productId") Long productId);
}