package kz.handshop.repository;

import kz.handshop.entity.DeliveryAddress;
import kz.handshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    List<DeliveryAddress> findByUser(User user);
    List<DeliveryAddress> findByUserId(Long userId);
}