package com.jobportal.auth.service;

import com.jobportal.auth.config.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JWTService {
    private final JWTProperties jwtProperties;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token with specified user details
     *
     * @param userId User identifier
     * @param email Email address
     * @return The generated JWT string
     */
    private String generateToken(UUID userId, String email) {
        Instant now = Instant.now();
        Instant expirationTime = now.plusMillis(jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * <i>Validates JWT token sent in request cookie with the defined signing key</i>
     * <p>
     *     <ul>
     *         <li>If the token is tampered it throws {@link JwtException} exception</li>
     *         <li>If token has passed its expiration time it throws {@link ExpiredJwtException} exception</li>
     *     </ul>
     * </p>
     *
     * @param token
     * @return {@link Claims} containing user information
     */
    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Creates a JWTCookie for the given user details
     *
     * @param userId User identifier
     * @param email Email address
     * @return A new JWTCookie instance with session attributes
     */
    public ResponseCookie createJWTCookie(UUID userId, String email) {
        String token = generateToken(userId, email);

        return ResponseCookie.from(jwtProperties.getCookieName(), token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(jwtProperties.getExpiration())
                .build();
    }

    /**
     * Clears the JWTCookie from the browser
     *
     * @return A cleared JWTCookie with expiration set to now
     */
    public ResponseCookie clearJWTCookie(){
        return ResponseCookie.from(jwtProperties.getCookieName(), "")
                .secure(false)
                .httpOnly(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
    }

    /**
     * Extracts the JWT token from cookies when generating response header
     *
     * @param request The incoming HTTP request
     * @return The extracted JWT string if found
     */
    public String extractJWTTokenFromCookie(HttpServletRequest request){
        if(request.getCookies() == null)
            return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(jwtProperties.getCookieName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
