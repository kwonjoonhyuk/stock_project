package zerobase.stock_project.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.stock_project.persist.entity.CompanyEntity;

import java.util.Optional;


public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    boolean existsByTicker(String ticker);

    Optional<CompanyEntity> findByName(String name);

    Page<CompanyEntity> findByNameStartingWithIgnoreCase(String keyword, Pageable pageable);

    Optional<CompanyEntity> findByTicker(String ticker);

}
