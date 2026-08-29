package com.dev.jwt_login.identity.inbound.graphql;

import com.dev.jwt_login.identity.inbound.model.response.UserResponse;
import com.dev.jwt_login.identity.inbound.usecase.GetUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserQueryResolver {

    private final GetUserUseCase getUserUseCase;

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<UserResponse> getAllUsers() {
        return getUserUseCase.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public UserResponse getUserById(@Argument Long id) {
        return getUserUseCase.findById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public UserResponse getUserByUsername(@Argument String username) {
        return getUserUseCase.findByUsername(username);
    }
}
