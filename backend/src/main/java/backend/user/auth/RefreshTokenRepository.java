package backend.user.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
