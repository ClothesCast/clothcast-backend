package ktb.clothcast.dao;

import ktb.clothcast.domain.Shoes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoesRepository extends JpaRepository<Shoes, Long> {
}
