package backend.user.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class RefreshTokenService {

    @Value("${refresh.expiration}")long expiration;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHash tokenHash;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, TokenHash tokenHash) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenHash = tokenHash;
    }

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateRefreshToken() {

        byte[] bytes = new byte[64];

        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public String createRefreshToken(UUID userId) {

        String refreshToken = generateRefreshToken();
        UUID tokenFamilyId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        RefreshToken refresh = new RefreshToken(
                userId,
                tokenHash.hashToken(refreshToken),
                tokenFamilyId,
                now,
                LocalDateTime.now().plusDays(expiration));
        refreshTokenRepository.save(refresh);
        return refreshToken;
    }

}