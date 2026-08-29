package com.dev.jwt_login.identity.inbound.graphql;

import com.dev.jwt_login.identity.inbound.model.request.CreateUserRequest;
import com.dev.jwt_login.identity.inbound.model.request.UpdateUserRequest;
import com.dev.jwt_login.identity.inbound.model.response.UserResponse;
import com.dev.jwt_login.identity.inbound.usecase.CreateUserUseCase;
import com.dev.jwt_login.identity.inbound.usecase.DeleteUserUseCase;
import com.dev.jwt_login.identity.inbound.usecase.UpdateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserMutationResolver {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public UserResponse createUser(@Argument String username,
                                   @Argument String email,
                                   @Argument String password) {
        return createUserUseCase.create(new CreateUserRequest(username, email, password));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public UserResponse updateUser(@Argument Long id,
                                   @Argument String email,
                                   @Argument String password,
                                   @Argument Boolean enabled) {
        return updateUserUseCase.update(id, new UpdateUserRequest(email, password, enabled));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        deleteUserUseCase.delete(id);
        return true;
    }
}
