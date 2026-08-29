package com.dev.jwt_login.identity.inbound.usecase.impl;

import com.dev.jwt_login.identity.domain.service.UserService;
import com.dev.jwt_login.identity.inbound.usecase.DeleteUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultDeleteUserUseCase implements DeleteUserUseCase {

    private final UserService userService;

    @Override
    public void delete(Long id) {
        userService.delete(id);
    }
}
