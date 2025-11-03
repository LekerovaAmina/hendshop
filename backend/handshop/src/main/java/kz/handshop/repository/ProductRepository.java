package kz.handshop.repository;

import kz.handshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(Product.ProductStatus status);
    List<Product> findByFreelancerId(Long freelancerId);
    List<Product> findByFreelancerIdAndStatus(Long freelancerId, Product.ProductStatus status);
    List<Product> findByShelfId(Long shelfId);

    @Query("SELECT p FROM Product p WHERE p.status = 'PUBLISHED' AND LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> searchPublished(@Param("query") String query);

    @Query("SELECT p FROM Product p WHERE p.status = 'MODERATION' ORDER BY p.createdAt ASC")
    List<Product> findPendingModeration();

    List<Product> findByStatusOrderByReportsCountDesc(Product.ProductStatus status);

    @Query("SELECT p FROM Product p WHERE p.status = 'PUBLISHED' ORDER BY p.viewsCount DESC")
    List<Product> findTopViewed();

    @Query("SELECT p FROM Product p WHERE p.freelancer.id = :freelancerId AND p.shelf.globalCategory.id = :categoryId")
    List<Product> findByFreelancerAndCategory(
            @Param("freelancerId") Long freelancerId,
            @Param("categoryId") Long categoryId
    );
}
