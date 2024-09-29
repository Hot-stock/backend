package com.bjcareer.userservice.application.auth.token.ports;

import java.util.List;

import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.domain.entity.RoleType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

public interface TonkenMangerUsecase {
	Long ACCESS_TOKEN_EXPIRE_DURATION_SEC = 30 * 60L; //30분
	Long REFRESH_TOKEN_EXPIRE_DURATION_SEC = 15 * 24 * 3500L; //15일
	JwtTokenVO generateToken(String sessionId, List<RoleType> roles);
	Claims verifyToken(String token) throws SignatureException, ExpiredJwtException;
	boolean validateRefreshTokenExpiration(String refreshToken) throws SignatureException, ExpiredJwtException;
}
