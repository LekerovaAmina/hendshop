package kz.handshop.repository;

import kz.handshop.entity.CategoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRequestRepository extends JpaRepository<CategoryRequest, Long> {
    List<CategoryRequest> findByIsReviewedFalse();
}