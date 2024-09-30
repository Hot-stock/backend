package com.bjcareer.userservice.application.auth.token;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bjcareer.userservice.application.auth.token.ports.TonkenManagerUsecase;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.domain.entity.RoleType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JWTManagerService implements TonkenManagerUsecase {
    private final SecretKey signingKey;

    public JWTManagerService(@Value("${secret.key}") String secretKey) {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public JwtTokenVO generateToken(String sessionId, List<RoleType> roles) {
        long currentTimeMillis = System.currentTimeMillis();

        String accessToken = generateToken(currentTimeMillis + ACCESS_TOKEN_EXPIRE_DURATION_SEC * 1000L);
        String refreshToken = generateToken(currentTimeMillis + REFRESH_TOKEN_EXPIRE_DURATION_SEC * 1000L);

        return new JwtTokenVO(accessToken, refreshToken, sessionId, currentTimeMillis + REFRESH_TOKEN_EXPIRE_DURATION_SEC * 1000L, roles);
    }

    public Claims verifyToken(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }

    public boolean validateRefreshTokenExpiration(String refreshToken) throws SignatureException, ExpiredJwtException{
        Claims claims = validateRefreshToken(refreshToken);
        boolean result = validatAccessTokenExpiration(claims.getExpiration());
        return result;
    }

    private Claims validateRefreshToken(String refreshToken) throws SignatureException, ExpiredJwtException {
        Claims payload = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(refreshToken).getPayload();
        return payload;
    }

    private boolean validatAccessTokenExpiration(Date expiration) throws SignatureException, ExpiredJwtException {
        if (expiration.after(new Date())) {
            return false;
        }

        return true;
    }

    private String generateToken(long expiration) {
        Date expirationDate = new Date(expiration);
        return Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(signingKey)
                .compact();
    }
}
