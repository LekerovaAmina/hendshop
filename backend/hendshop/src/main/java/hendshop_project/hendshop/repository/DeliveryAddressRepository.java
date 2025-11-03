package kz.handshop.repository;

import kz.handshop.model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    List<DeliveryAddress> findByUserId(Long userId);
    Optional<DeliveryAddress> findByUserIdAndIsDefaultTrue(Long userId);

    @Query("SELECT da FROM DeliveryAddress da WHERE da.user.id = :userId ORDER BY da.isDefault DESC, da.createdAt DESC")
    List<DeliveryAddress> findByUserIdOrderByDefault(@Param("userId") Long userId);
}
