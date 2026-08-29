package com.dev.jwt_login.identity.inbound.usecase;

import com.dev.jwt_login.identity.inbound.model.request.CreateUserRequest;
import com.dev.jwt_login.identity.inbound.model.response.UserResponse;

public interface CreateUserUseCase {

    UserResponse create(CreateUserRequest request);
}
