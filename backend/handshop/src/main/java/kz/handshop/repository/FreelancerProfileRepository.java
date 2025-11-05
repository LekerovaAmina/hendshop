package kz.handshop.repository;

import kz.handshop.entity.FreelancerProfile;
import kz.handshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {
    Optional<FreelancerProfile> findByUser(User user);
    Optional<FreelancerProfile> findByUserId(Long userId);
}
