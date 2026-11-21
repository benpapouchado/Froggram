package backend.user.auth;

import java.util.Optional;
import java.time.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Query("""
            SELECT id, userId, token_family_id, token_hash
            FROM RefreshToken rt
            WHERE rt.refresh_token = :refreshToken
            """)
    Optional<RefreshToken> findOldToken(@Param("refresh_token") String refreshToken);

    @Query("""
            UPDATE RefreshToken rt
            SET rt.revoked_at = :now
            AND rt.replaced_by = :replacedBy
            WHERE rt.id = id
            """)
    Optional<RefreshToken> rotaterefreshToken(
            @Param("id") Long id,
            @Param("replaced_by") Long replacedBy,
            @Param("revoked_at") LocalDateTime now);
}
