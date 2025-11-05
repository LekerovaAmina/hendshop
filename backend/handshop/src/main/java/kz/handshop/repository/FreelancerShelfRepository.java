package kz.handshop.repository;

import kz.handshop.entity.FreelancerShelf;
import kz.handshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FreelancerShelfRepository extends JpaRepository<FreelancerShelf, Long> {
    List<FreelancerShelf> findByFreelancer(User freelancer);
    List<FreelancerShelf> findByFreelancerId(Long freelancerId);
}