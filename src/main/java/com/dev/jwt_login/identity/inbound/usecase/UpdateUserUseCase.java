package com.dev.jwt_login.identity.inbound.usecase;

import com.dev.jwt_login.identity.inbound.model.request.UpdateUserRequest;
import com.dev.jwt_login.identity.inbound.model.response.UserResponse;

public interface UpdateUserUseCase {

    UserResponse update(Long id, UpdateUserRequest request);
}
