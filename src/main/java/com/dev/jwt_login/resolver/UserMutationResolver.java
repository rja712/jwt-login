package com.dev.jwt_login.resolver;

import com.dev.jwt_login.dto.CreateUserRequest;
import com.dev.jwt_login.dto.UpdateUserRequest;
import com.dev.jwt_login.dto.UserDto;
import com.dev.jwt_login.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserMutationResolver {

    private final UserService userService;

    @MutationMapping
    public UserDto createUser(@Argument String username, 
                              @Argument String email, 
                              @Argument String password) {
        CreateUserRequest request = new CreateUserRequest(username, email, password);
        return userService.createUser(request);
    }

    @MutationMapping
    public UserDto updateUser(@Argument Long id, 
                              @Argument String email, 
                              @Argument String password, 
                              @Argument Boolean enabled) {
        UpdateUserRequest request = new UpdateUserRequest(email, password, enabled);
        return userService.updateUser(id, request);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userService.deleteUser(id);
        return true;
    }
}
