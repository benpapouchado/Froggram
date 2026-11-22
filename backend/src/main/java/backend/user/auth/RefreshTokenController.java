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
            String refreshToken,
            String userId) {
    }

    public record RefreshTokenResponse(
            String refreshToken,
            String accessToken) {
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenDto entity) {
        Optional<RefreshToken> oldToken = refreshTokenRepository.findByTokenHash(
            tokenHash.hashToken(entity.refreshToken));

        if(!tokenHash.hashToken(entity.refreshToken).equals(oldToken.orElseThrow().getTokenHash())){
            throw new IllegalArgumentException("Different token");
        }

        String[] rotateRefreshtoken = refreshTokenService.refreshToken(
            oldToken.orElseThrow().getId(),
            LocalDateTime.now(),
            oldToken.orElseThrow().getUserId(),
            oldToken.orElseThrow().getTokenFamilyId()
        );

        String accessToken = rotateRefreshtoken[0];
        String refreshToken = rotateRefreshtoken[1];

        return ResponseEntity.ok().body(new RefreshTokenResponse(
                refreshToken,
                accessToken));
    }

}

// curl -i -X POST http://localhost:8080/token/refresh -H "Content-Type: application/json" -d '{
//   "refreshToken": "9fa66fc55627930f3b7cd2fa39af3c6904b4c3d8e68691c0f0781f6a136dd913",
//   "userId": "892e701c-dd1f-46ac-9302-31582771666c"
// }'

