package com.dev.jwt_login.identity.inbound.rest;

import com.dev.jwt_login.identity.inbound.model.request.LoginRequest;
import com.dev.jwt_login.identity.inbound.model.response.LoginResponse;
import com.dev.jwt_login.identity.inbound.usecase.LoginUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginUseCase loginUseCase;

    @PostMapping()
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return loginUseCase.login(request);
    }
}
