package com.dev.jwt_login.identity.inbound.usecase.impl;

import com.dev.jwt_login.identity.domain.event.DomainEvent;
import com.dev.jwt_login.identity.domain.model.entity.User;
import com.dev.jwt_login.identity.domain.port.UserPersistencePort;
import com.dev.jwt_login.identity.domain.service.UserService;
import com.dev.jwt_login.identity.inbound.model.mapper.UserDomainMapper;
import com.dev.jwt_login.identity.inbound.model.request.CreateUserRequest;
import com.dev.jwt_login.identity.inbound.model.response.UserResponse;
import com.dev.jwt_login.identity.inbound.usecase.CreateUserUseCase;
import com.dev.jwt_login.identity.outbound.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultCreateUserUseCase implements CreateUserUseCase {

    private final UserService userService;
    private final UserPersistencePort userPersistencePort;
    private final DomainEventPublisher eventPublisher;
    private final UserDomainMapper userMapper;

    @Override
    public UserResponse create(CreateUserRequest request) {
        User user = userService.create(request.username(), request.email(), request.password());
        User saved = userPersistencePort.save(user);

        publishEvents(user.getDomainEvents());
        user.clearDomainEvents();

        return userMapper.toDto(saved);
    }

    private void publishEvents(List<DomainEvent> events) {
        events.forEach(eventPublisher::publish);
    }
}
