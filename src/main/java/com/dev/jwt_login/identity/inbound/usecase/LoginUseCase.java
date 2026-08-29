package com.dev.jwt_login.identity.inbound.usecase;

import com.dev.jwt_login.identity.inbound.model.request.LoginRequest;
import com.dev.jwt_login.identity.inbound.model.response.LoginResponse;

public interface LoginUseCase {

    LoginResponse login(LoginRequest request);
}
