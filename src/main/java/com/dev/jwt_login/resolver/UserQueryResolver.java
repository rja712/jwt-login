package com.dev.jwt_login.resolver;

import com.dev.jwt_login.dto.UserDto;
import com.dev.jwt_login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserQueryResolver {

    private final UserService userService;

    @QueryMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @QueryMapping
    public UserDto getUserById(@Argument Long id) {
        return userService.getUserById(id);
    }

    @QueryMapping
    public UserDto getUserByUsername(@Argument String username) {
        return userService.getUserByUsername(username);
    }
}
