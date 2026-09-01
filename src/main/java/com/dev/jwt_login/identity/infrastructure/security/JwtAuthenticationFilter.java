package com.dev.jwt_login.identity.infrastructure.security;

import com.dev.jwt_login.identity.infrastructure.token.TokenBlacklist;
import com.dev.jwt_login.identity.infrastructure.token.TokenClaims;
import com.dev.jwt_login.identity.infrastructure.token.TokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenValidator tokenValidator;
    private final TokenBlacklist tokenBlacklist;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (tokenBlacklist.isBlacklisted(token)) {
                log.debug("Blacklisted token used on {} {}", request.getMethod(), request.getRequestURI());
            } else {
                TokenClaims claims = tokenValidator.validate(token);

                if (claims == null || claims.username() == null) {
                    // Not an error: an expired or malformed token simply leaves the request
                    // unauthenticated, and Spring Security answers 401 further down the chain.
                    log.debug("Rejected token on {} {}", request.getMethod(), request.getRequestURI());
                } else {
                    List<SimpleGrantedAuthority> authorities = claims.roles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(claims.username(), null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
