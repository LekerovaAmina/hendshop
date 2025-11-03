package kz.handshop.repository;

import kz.handshop.model.FreelancerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {
    Optional<FreelancerProfile> findByUserId(Long userId);
    List<FreelancerProfile> findByRatingGreaterThanEqual(Double rating);

    @Query("SELECT f FROM FreelancerProfile f ORDER BY f.rating DESC")
    List<FreelancerProfile> findTopRated();
}
