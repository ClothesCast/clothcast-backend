package ktb.clothcast.dao;

import ktb.clothcast.domain.Outerwear;
import ktb.clothcast.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OuterwearRepository extends JpaRepository<Outerwear, Long> {
    Optional<Outerwear> findByUser(User user);
}
