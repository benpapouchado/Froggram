package backend.user.auth;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final JwtService jwtService;

    public TokenController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // Generate JWT
    @GetMapping("/generate")
    public Map<String, String> generateToken(
            @RequestParam String username) {

        String token = jwtService.createAccessToken(username);

        return Map.of("token", token);
    }

    // Validate JWT
    @PostMapping("/validate")
    public Map<String, Object> validateToken(
            @RequestParam String token) {

        try {
            String username =
                    jwtService.extractUsername(token);

            return Map.of(
                    "valid", true,
                    "username", username
            );

        } catch (Exception e) {

            return Map.of(
                    "valid", false,
                    "message", "Invalid or expired token"
            );
        }
    }
}
