package com.dev.jwt_login.identity.inbound.usecase.impl;

import com.dev.jwt_login.identity.domain.exception.UserNotFoundException;
import com.dev.jwt_login.identity.domain.model.entity.User;
import com.dev.jwt_login.identity.domain.model.valueobject.Username;
import com.dev.jwt_login.identity.domain.port.UserPersistencePort;
import com.dev.jwt_login.identity.inbound.model.mapper.UserDomainMapper;
import com.dev.jwt_login.identity.inbound.model.response.UserResponse;
import com.dev.jwt_login.identity.inbound.usecase.GetUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultGetUserUseCase implements GetUserUseCase {

    private final UserPersistencePort userPersistencePort;
    private final UserDomainMapper userMapper;

    @Override
    public List<UserResponse> findAll() {
        return userPersistencePort.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userPersistencePort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponse findByUsername(String username) {
        User user = userPersistencePort.findByUsername(new Username(username))
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }
}
