package com.bjcareer.userservice.security;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.bjcareer.userservice.application.auth.token.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.application.auth.token.valueObject.JwtTokenVO;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.out.persistence.CacheTokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Aspect
@Slf4j
public class RoleAspect {
    private final CacheTokenRepository tokenRepository;

    @Before("@annotation(hasRole) && args(request, ..)")
    public void checkRole(HasRole hasRole, HttpServletRequest request) {
        RoleType[] requiredRoles = hasRole.value();

        log.debug("Roles required: {}", Arrays.toString(requiredRoles));

        if (requiredRoles.length == 0) {
            log.warn("No roles specified. Unauthorized access attempt.");
            throw new UnauthorizedAccessAttemptException("User not authenticated");
        }

        if (Arrays.asList(requiredRoles).contains(RoleType.ALL)) {
            log.debug("Access granted to all roles.");
            return;
        }

        String sessionId = (String) request.getAttribute("sessionId");
        log.debug("Session ID: {}", sessionId);

        JwtTokenVO authToken = tokenRepository.findTokenBySessionId(sessionId)
            .orElseThrow(() -> {
                log.warn("Session ID not found or user not authenticated.");
                return new UnauthorizedAccessAttemptException("User not authenticated");
            });

        checkUserRoles(requiredRoles, authToken.getRoleType());
    }

    private void checkUserRoles(RoleType[] requiredRoles, List<RoleType> userRoles) {
        boolean hasRequiredRole = userRoles.stream()
            .anyMatch(userRole -> Arrays.stream(requiredRoles)
                .anyMatch(requiredRole -> requiredRole.equals(userRole)));

        if (!hasRequiredRole) {
            log.warn("User does not have the required role. Access denied.");
            throw new UnauthorizedAccessAttemptException("You do not have the required role to access this resource");
        }

        log.debug("Access granted.");
    }
}
