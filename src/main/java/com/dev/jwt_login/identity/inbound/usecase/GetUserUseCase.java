package com.dev.jwt_login.identity.inbound.usecase;

import com.dev.jwt_login.identity.inbound.model.response.UserResponse;

import java.util.List;

public interface GetUserUseCase {

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse findByUsername(String username);
}
