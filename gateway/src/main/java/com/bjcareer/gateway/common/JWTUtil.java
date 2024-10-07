package com.bjcareer.gateway.common;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JWTUtil {
	private final SecretKey signingKey;

	public JWTUtil(@Value("${secret.key}") String secretKey) {
		signingKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
	}

	public TokenValidationResult validateToken(String token) {
		try {
			parseToken(token);
			return new TokenValidationResult(true, false, "Valid JWT token");
		} catch (MalformedJwtException e) {
			return new TokenValidationResult(false, false, "Invalid JWT token");
		} catch (SignatureException e) {
			return new TokenValidationResult(false, false, "Invalid JWT signature");
		} catch (ExpiredJwtException e) {
			return new TokenValidationResult(false, true, "JWT token has expired");
		}
	}

	public Claims parseToken(String token) {
		return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
	}
}
