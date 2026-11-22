package backend.user.auth;

import java.util.Optional;
import java.time.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("""
            UPDATE RefreshToken rt
            SET rt.revokedAt = :revokedAt,
                rt.replacedBy = :replacedBy
            WHERE rt.id = id
            """)
    Optional<RefreshToken> rotaterefreshToken(
            @Param("id") Long id,
            @Param("replacedBy") Long replacedBy,
            @Param("revokedAt") LocalDateTime revokedAt);
}
