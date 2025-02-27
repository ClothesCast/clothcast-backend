package ktb.clothcast.dao;

import ktb.clothcast.domain.Shoes;
import ktb.clothcast.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShoesRepository extends JpaRepository<Shoes, Long> {
    Optional<Shoes> findByUser(User user);
}
