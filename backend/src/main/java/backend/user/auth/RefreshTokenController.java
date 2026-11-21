package backend.user.auth;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.user.auth.RefreshTokenController.RefreshTokenDto;
import backend.user.auth.RefreshTokenController.RefreshTokenResponse;

@RestController
@RequestMapping("/token")
public class RefreshTokenController {

    private final TokenHash tokenHash;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenController(
            TokenHash tokenHash,
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository,
            RefreshTokenService refreshTokenService) {
        this.tokenHash = tokenHash;
        this.jwtService = jwtService;
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
        Optional<RefreshToken> oldToken = refreshTokenRepository.findOldToken(entity.refreshToken);

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
