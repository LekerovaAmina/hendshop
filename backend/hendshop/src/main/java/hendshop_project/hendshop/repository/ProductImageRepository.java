package kz.handshop.repository;

import kz.handshop.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductIdOrderByOrderIndexAsc(Long productId);
    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId);
    void deleteByProductId(Long productId);
}
