package kz.handshop.repository;

import kz.handshop.entity.Product;
import kz.handshop.entity.ProductModeration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductModerationRepository extends JpaRepository<ProductModeration, Long> {
    List<ProductModeration> findByProduct(Product product);
    List<ProductModeration> findByProductId(Long productId);
    List<ProductModeration> findByProductIdOrderByCreatedAtDesc(Long productId);
}