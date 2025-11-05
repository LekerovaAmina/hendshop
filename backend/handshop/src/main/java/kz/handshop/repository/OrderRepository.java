package kz.handshop.repository;

import kz.handshop.entity.Order;
import kz.handshop.entity.OrderStatus;
import kz.handshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByUserId(Long userId);
    List<Order> findByFreelancer(User freelancer);
    List<Order> findByFreelancerId(Long freelancerId);
    List<Order> findByFreelancerIdAndStatus(Long freelancerId, OrderStatus status);
    Integer countByFreelancerIdAndStatusIn(Long freelancerId, List<OrderStatus> statuses);
}