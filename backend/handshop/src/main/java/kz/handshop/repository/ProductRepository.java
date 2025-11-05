package kz.handshop.repository;

import kz.handshop.entity.Product;
import kz.handshop.entity.ProductStatus;
import kz.handshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByFreelancer(User freelancer);
    List<Product> findByFreelancerId(Long freelancerId);
    List<Product> findByFreelancerIdAndStatus(Long freelancerId, ProductStatus status);

    @Query("SELECT p FROM Product p WHERE p.status = 'PUBLISHED' AND " +
            "(:categoryId IS NULL OR p.shelf.globalCategory.id = :categoryId) AND " +
            "(:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Product> findPublishedProducts(@Param("categoryId") Long categoryId,
                                        @Param("search") String search);

    List<Product> findByStatusAndReportsCountGreaterThanEqual(ProductStatus status, Integer reportsCount);
}
