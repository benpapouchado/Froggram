package backend.user.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HexFormat;
import java.security.*;
import java.util.Base64;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;
    private static final String ISSUER = "froggram";

    public JwtService(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
            Base64.getDecoder().decode(secret)
        );
        this.expiration = expiration;
    }

    public String createAccessToken(String userId) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
            .subject(userId)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .issuer(ISSUER)
            .compact();
    }

    public String hashToken(String token) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hash = digest.digest(
            token.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );

        return HexFormat.of().formatHex(hash);

    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException(e);
    }
}

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}

