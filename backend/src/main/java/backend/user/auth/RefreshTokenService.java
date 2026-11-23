package backend.user.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    @Value("${refresh.expiration}")
    long expiration;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHash tokenHash;

    public RefreshTokenService(JwtService jwtService, RefreshTokenRepository refreshTokenRepository,
            TokenHash tokenHash) {
        this.jwtService = jwtService;
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

    public String createRefreshTokenOnLogin(UUID userId) {

        String refreshToken = generateRefreshToken();
        UUID tokenFamilyId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        RefreshToken refresh = new RefreshToken(
                userId,
                tokenHash.hashToken(refreshToken),
                tokenFamilyId,
                now,
                now.plusDays(expiration),
                null);
        refreshTokenRepository.save(refresh);
        return refreshToken;
    }

    @Transactional
    public String[] refreshToken(Long oldId, LocalDateTime now, UUID userId, UUID tokenFamilyId) {
        String[] tokens = new String[3];
        String newAccessToken = jwtService.createAccessToken(userId.toString());
        String newRefreshToken = generateRefreshToken();
        tokens[0] = newAccessToken;
        tokens[1] = newRefreshToken;

        RefreshToken rotateToken = new RefreshToken(
                userId,
                tokenHash.hashToken(newRefreshToken),
                tokenFamilyId,
                now,
                now.plusDays(expiration),
                null);

        RefreshToken newToken = refreshTokenRepository.save(rotateToken);
        Long replacedBy = newToken.getId();

        tokens[2] = replacedBy.toString();
        return tokens;
    }

}
