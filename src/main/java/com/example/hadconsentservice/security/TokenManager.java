package com.example.hadconsentservice.security;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenManager implements Serializable {
    /**
     *
     */
    private static final Duration JWT_TOKEN_VALIDITY = Duration.ofMinutes(30);

    private Algorithm hmac512;
    private  JWTVerifier verifier;
    @Value("${secret}")
    String secret;
    public TokenManager(@Value("${secret}") final String secret) {
        this.hmac512 = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(this.hmac512).build();
    }

    public String generateToken(final UserDetails userDetails) {
        final Instant now = Instant.now();
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuer("app")
                .withIssuedAt(now)
                .withExpiresAt(now.plusMillis(JWT_TOKEN_VALIDITY.toMillis()))
                .sign(this.hmac512);
    }

    public boolean validateTokenAndGetUsername(final String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        Boolean isTokenExpired = claims.getExpiration().before(new Date());
        return (username.equals(userDetails.getUsername()) && !isTokenExpired);
    }
    public String getUsernameFromToken(String token) {
        final Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
