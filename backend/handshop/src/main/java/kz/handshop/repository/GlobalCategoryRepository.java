package kz.handshop.repository;

import kz.handshop.entity.GlobalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GlobalCategoryRepository extends JpaRepository<GlobalCategory, Long> {
    List<GlobalCategory> findByIsActiveTrue();
    Boolean existsByName(String name);
}
