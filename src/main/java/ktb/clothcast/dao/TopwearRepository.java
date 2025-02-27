package ktb.clothcast.dao;

import ktb.clothcast.domain.Topwear;
import ktb.clothcast.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TopwearRepository extends JpaRepository<Topwear, Long> {
    Optional<Topwear> findByUser(User user);
}
