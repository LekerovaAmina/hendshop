package kz.handshop.repository;

import kz.handshop.model.ProductModeration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductModerationRepository extends JpaRepository<ProductModeration, Long> {
    List<ProductModeration> findByProductId(Long productId);
    List<ProductModeration> findByAdminId(Long adminId);

    @Query("SELECT pm FROM ProductModeration pm WHERE pm.product.id = :productId ORDER BY pm.createdAt DESC")
    List<ProductModeration> findByProductIdOrderByCreatedAtDesc(@Param("productId") Long productId);

    @Query("SELECT pm FROM ProductModeration pm WHERE pm.product.id = :productId ORDER BY pm.createdAt DESC LIMIT 1")
    Optional<ProductModeration> findLatestByProductId(@Param("productId") Long productId);
}
