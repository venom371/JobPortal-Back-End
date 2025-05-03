package com.jobportal.common;

import org.springframework.stereotype.Component;

import com.jobportal.exceptions.JWTException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;

import org.springframework.beans.factory.annotation.Value;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(String userId) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public void validateToken(String authToken) {
        try {
            Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new JWTException("Token has expired");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new JWTException("Malformed token");
        } catch (Exception e) {
            throw new JWTException("Invalid token");
        }
    }

    public String getUserIdFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
