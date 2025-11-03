package kz.handshop.repository;

import kz.handshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByFreelancerId(Long freelancerId);
    List<Order> findByStatus(Order.OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.freelancer.id = :freelancerId AND o.status != 'DELIVERED'")
    List<Order> findActiveByFreelancerId(@Param("freelancerId") Long freelancerId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.freelancer.id = :freelancerId AND o.status != 'DELIVERED'")
    Long countActiveByFreelancerId(@Param("freelancerId") Long freelancerId);

    List<Order> findByUserIdAndStatus(Long userId, Order.OrderStatus status);
    List<Order> findByFreelancerIdAndStatus(Long freelancerId, Order.OrderStatus status);
}
