package com.bjcareer.userservice.application.auth.token;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.application.auth.token.valueObject.TokenValidationResult;
import com.bjcareer.userservice.domain.entity.RoleType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JWTUtil {
    public static final int ACCESS_TOKEN_EXPIRE_DURATION_SEC = 30 * 60 * 1000; //30분
    public static final int REFRESH_TOKEN_EXPIRE_DURATION_SEC = 15 * 24 * 3500 * 1000; //15일

    private final SecretKey signingKey;

    public JWTUtil(@Value("${secret.key}") String secretKey) {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public JwtTokenVO generateToken(String email, String sessionId, List<RoleType> roles) {
        long currentTimeMillis = System.currentTimeMillis();

        String accessToken = generateToken(email, currentTimeMillis + ACCESS_TOKEN_EXPIRE_DURATION_SEC);
        String refreshToken = generateToken(email, currentTimeMillis + REFRESH_TOKEN_EXPIRE_DURATION_SEC);

        return new JwtTokenVO(accessToken, refreshToken, sessionId, currentTimeMillis + REFRESH_TOKEN_EXPIRE_DURATION_SEC * 1000L, roles);
    }

    public TokenValidationResult validateToken(String token) {
        try {
            parseToken(token);
            return new TokenValidationResult(true, false, "Valid JWT token");
        } catch (MalformedJwtException e){
            return new TokenValidationResult(false, false, "Invalid JWT token");
        }catch (SignatureException e) {
            return new TokenValidationResult(false, false, "Invalid JWT signature");
        }
        catch (ExpiredJwtException e) {
            return new TokenValidationResult(false, true, "JWT token has expired");
        }
    }

    public Claims parseToken(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }

    private String generateToken(String email, long expiration) {
        Date expirationDate = new Date(expiration);
        return Jwts.builder()
            .subject(email)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(signingKey)
                .compact();
    }
}
