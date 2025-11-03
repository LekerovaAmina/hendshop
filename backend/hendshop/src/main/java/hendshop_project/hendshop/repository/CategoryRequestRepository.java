package kz.handshop.repository;

import kz.handshop.model.CategoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRequestRepository extends JpaRepository<CategoryRequest, Long> {
    List<CategoryRequest> findByIsReviewedFalse();
    List<CategoryRequest> findByUserId(Long userId);

    @Query("SELECT cr.requestedCategoryName, COUNT(cr) FROM CategoryRequest cr WHERE cr.isReviewed = false GROUP BY cr.requestedCategoryName ORDER BY COUNT(cr) DESC")
    List<Object[]> findMostRequestedCategories();
}
