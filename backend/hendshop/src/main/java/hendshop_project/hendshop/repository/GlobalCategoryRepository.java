package kz.handshop.repository;

import kz.handshop.model.GlobalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GlobalCategoryRepository extends JpaRepository<GlobalCategory, Long> {
    List<GlobalCategory> findByIsActiveTrue();
    Optional<GlobalCategory> findByName(String name);
}
