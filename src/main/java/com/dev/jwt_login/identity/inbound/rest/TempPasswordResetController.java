package com.dev.jwt_login.identity.inbound.rest;

import com.dev.jwt_login.identity.domain.model.valueobject.Password;
import com.dev.jwt_login.identity.domain.model.valueobject.Username;
import com.dev.jwt_login.identity.domain.port.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Temporary endpoint to reset the admin password.
 * Remove after the password has been reset.
 */
@RestController
@RequestMapping("/api/temp/reset-admin-password")
@RequiredArgsConstructor
public class TempPasswordResetController {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<String> resetAdminPassword() {
        userPersistencePort.findByUsername(new Username("admin_user"))
                .ifPresent(user -> {
                    user.changePassword(Password.fromRaw("password123", passwordEncoder));
                    userPersistencePort.save(user);
                });
        return ResponseEntity.ok("Admin password reset attempted");
    }
}
