package com.bjcareer.userservice.domain;

import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.service.vo.JwtTokenVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class AuthTokenManager {
    private final SecretKey signingKey;
    public static final Long ACCESS_TOKEN_EXPIRE_DURATION_SEC = 30 * 60L; //30분
    public static final Long REFRESH_TOKEN_EXPIRE_DURATION_SEC = 15 * 24 * 3500L; //15일

    public AuthTokenManager(@Value("${secret.key}") String secretKey) {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    public JwtTokenVO generateToken(String sessionId, List<RoleType> roles) {
        long currentTimeMillis = System.currentTimeMillis();

        String accessToken = generateToken(currentTimeMillis + ACCESS_TOKEN_EXPIRE_DURATION_SEC * 1000L);
        String refreshToken = generateToken(currentTimeMillis + REFRESH_TOKEN_EXPIRE_DURATION_SEC * 1000L);

        return new JwtTokenVO(accessToken, refreshToken, sessionId, currentTimeMillis + REFRESH_TOKEN_EXPIRE_DURATION_SEC * 1000L, roles);
    }

    public Claims verifyToken(String token) throws SignatureException, ExpiredJwtException {
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
