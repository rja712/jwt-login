package com.dev.jwt_login.identity.domain.service;

import com.dev.jwt_login.identity.domain.exception.InvalidCredentialsException;
import com.dev.jwt_login.identity.domain.exception.UserNotFoundException;
import com.dev.jwt_login.identity.domain.model.entity.User;
import com.dev.jwt_login.identity.domain.model.valueobject.Username;
import com.dev.jwt_login.identity.domain.port.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginService {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoder passwordEncoder;

    public User login(String username, String password) {
        User user = userPersistencePort.findByUsername(new Username(username))
                .orElseThrow(() -> new UserNotFoundException("Invalid username or password"));

        if (!user.isEnabled()) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        if (!user.getPassword().matches(password, passwordEncoder)) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        user.recordAuthentication();

        return user;
    }
}
