package ktb.clothcast.dao;

import ktb.clothcast.domain.Bottomwear;
import ktb.clothcast.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BottomwearRepository extends JpaRepository<Bottomwear, Long> {
    Optional<Bottomwear> findByUser(User user);
}
