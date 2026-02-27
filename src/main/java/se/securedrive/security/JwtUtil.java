package se.securedrive.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility-klass för att hantera JSON Web Tokens (JWT).
 * Används för att skapa tokens vid inloggning och extrahera användarinformation från inkommande tokens.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 timme

    /**
     * Genererar en JWT-token för en användare.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extraherar användarnamn från en JWT-token.
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
