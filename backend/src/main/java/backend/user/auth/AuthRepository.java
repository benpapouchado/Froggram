package backend.user.auth;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    List<Auth> findByUserId(UUID userId);
}
