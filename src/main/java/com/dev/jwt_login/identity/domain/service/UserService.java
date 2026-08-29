package com.dev.jwt_login.identity.domain.service;

import com.dev.jwt_login.identity.domain.exception.DuplicateEmailException;
import com.dev.jwt_login.identity.domain.exception.DuplicateUsernameException;
import com.dev.jwt_login.identity.domain.exception.UserNotFoundException;
import com.dev.jwt_login.identity.domain.model.entity.Role;
import com.dev.jwt_login.identity.domain.model.entity.User;
import com.dev.jwt_login.identity.domain.model.valueobject.Email;
import com.dev.jwt_login.identity.domain.model.valueobject.Password;
import com.dev.jwt_login.identity.domain.model.valueobject.RoleName;
import com.dev.jwt_login.identity.domain.model.valueobject.Username;
import com.dev.jwt_login.identity.domain.port.RolePersistencePort;
import com.dev.jwt_login.identity.domain.port.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserPersistencePort userPersistencePort;
    private final RolePersistencePort rolePersistencePort;
    private final PasswordEncoder passwordEncoder;

    public User create(String username, String email, String rawPassword) {
        if (userPersistencePort.existsByUsername(username)) {
            throw new DuplicateUsernameException(username);
        }
        if (userPersistencePort.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }

        Role defaultRole = rolePersistencePort.findByName(new RoleName("ROLE_USER"))
                .orElseThrow(() -> new IllegalStateException("Default role ROLE_USER not found"));

        User user = User.create(
                new Username(username),
                new Email(email),
                Password.fromRaw(rawPassword, passwordEncoder)
        );
        user.grantRole(defaultRole);
        return user;
    }

    public User update(Long id, String email, String rawPassword, Boolean enabled) {
        User user = userPersistencePort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (email != null) {
            if (!user.getEmail().value().equals(email) && userPersistencePort.existsByEmail(email)) {
                throw new DuplicateEmailException(email);
            }
            user.changeEmail(new Email(email));
        }

        if (rawPassword != null) {
            user.changePassword(Password.fromRaw(rawPassword, passwordEncoder));
        }

        if (enabled != null) {
            user.setEnabled(enabled);
        }

        return user;
    }

    public void delete(Long id) {
        if (!userPersistencePort.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userPersistencePort.deleteById(id);
    }
}
