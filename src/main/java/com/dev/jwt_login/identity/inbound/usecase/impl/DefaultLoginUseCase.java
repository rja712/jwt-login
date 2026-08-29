package com.dev.jwt_login.identity.inbound.usecase.impl;

import com.dev.jwt_login.identity.domain.event.DomainEvent;
import com.dev.jwt_login.identity.domain.model.entity.User;
import com.dev.jwt_login.identity.domain.service.LoginService;
import com.dev.jwt_login.identity.inbound.model.request.LoginRequest;
import com.dev.jwt_login.identity.inbound.model.response.LoginResponse;
import com.dev.jwt_login.identity.inbound.usecase.LoginUseCase;
import com.dev.jwt_login.identity.infrastructure.token.TokenGenerator;
import com.dev.jwt_login.identity.outbound.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultLoginUseCase implements LoginUseCase {

    private final LoginService loginService;
    private final TokenGenerator tokenGenerator;
    private final DomainEventPublisher eventPublisher;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = loginService.login(request.username(), request.password());

        List<String> roles = user.getRoles().stream()
                .map(role -> role.name().value())
                .collect(Collectors.toList());

        String token = tokenGenerator.generate(user.getUsername().value(), roles);

        publishEvents(user.getDomainEvents());
        user.clearDomainEvents();

        return new LoginResponse(token);
    }

    private void publishEvents(List<DomainEvent> events) {
        events.forEach(eventPublisher::publish);
    }
}
