package com.bjcareer.userservice.security;

import com.bjcareer.userservice.domain.entity.RoleAssignments;
import com.bjcareer.userservice.domain.entity.RoleType;
import com.bjcareer.userservice.exceptions.UnauthorizedAccessAttemptException;
import com.bjcareer.userservice.repository.RedisRepository;
import com.bjcareer.userservice.service.vo.JwtTokenVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Aspect
public class RoleAspect {
    private final RedisRepository redisRepository;

    @Before("@annotation(hasRole) && args(request, ..)")
    public void checkRole(HasRole hasRole, HttpServletRequest request) {
        RoleType[] roles = hasRole.value();

        System.out.println("Roles required: " + Arrays.toString(roles));

        if (roles.length == 0) {
            System.out.println("No roles specified. Unauthorized access attempt.");
            throw new UnauthorizedAccessAttemptException("User not authenticated");
        }

        boolean allowAll = Arrays.stream(roles).anyMatch(role -> role.equals(RoleType.ALL));

        if (allowAll) {
            System.out.println("Access granted to all roles.");
            return;
        }

        System.out.println("Session ID: " + request.getAttribute("sessionId"));
        String sessionId = (String) request.getAttribute("sessionId");

        Optional<JwtTokenVO> authTokenBySessionId = redisRepository.findAuthTokenBySessionId(sessionId);

        if (!authTokenBySessionId.isPresent()) {
            System.out.println("Session ID not found or user not authenticated.");
            throw new UnauthorizedAccessAttemptException("User not authenticated");
        }

        List<RoleType> roleTypes = authTokenBySessionId.get().getRoleType();


        boolean hasRequiredRole = roleTypes.stream()
                .anyMatch(assignment -> Arrays.stream(roles)
                        .anyMatch(role -> role.equals(assignment)));

        if (!hasRequiredRole) {
            System.out.println("User does not have the required role. Access denied.");
            throw new UnauthorizedAccessAttemptException("You do not have the required role to access this resource");
        }

        System.out.println("Access granted.");
    }
}
