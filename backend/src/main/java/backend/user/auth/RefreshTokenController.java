package backend.user.auth;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class RefreshTokenController {

    private final TokenHash tokenHash;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenController(
            TokenHash tokenHash,
            RefreshTokenRepository refreshTokenRepository,
            RefreshTokenService refreshTokenService) {
        this.tokenHash = tokenHash;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
    }

    public record RefreshTokenDto(
            String refreshToken) {
    }

    public record RefreshTokenResponse(
            String refreshToken,
            String accessToken) {
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenDto entity) {
        RefreshToken oldToken = refreshTokenRepository
                .findByTokenHash(entity.refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        String[] rotateRefreshtoken = refreshTokenService.refreshToken(
                oldToken.getId(),
                LocalDateTime.now(),
                oldToken.getUserId(),
                oldToken.getTokenFamilyId());

        String accessToken = rotateRefreshtoken[0];
        String refreshToken = rotateRefreshtoken[1];

        oldToken.setRevokedAt(LocalDateTime.now());
        oldToken.setReplacedBy(Long.parseLong(rotateRefreshtoken[2]));
        refreshTokenRepository.save(oldToken);

        return ResponseEntity.ok().body(new RefreshTokenResponse(
                refreshToken,
                accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> revokeToken(@RequestBody String refreshToken) {
        RefreshToken currentToken = refreshTokenRepository
                .findByTokenHash(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

                currentToken.setRevokedAt(LocalDateTime.now());
                refreshTokenRepository.save(currentToken);

        return ResponseEntity.ok().build();
    }

}
