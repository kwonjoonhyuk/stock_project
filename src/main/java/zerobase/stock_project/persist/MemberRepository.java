package zerobase.stock_project.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.stock_project.model.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
