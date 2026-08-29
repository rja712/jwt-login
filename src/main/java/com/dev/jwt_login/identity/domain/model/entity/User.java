package com.dev.jwt_login.identity.domain.model.entity;

import com.dev.jwt_login.identity.domain.event.DomainEvent;
import com.dev.jwt_login.identity.domain.event.UserAuthenticatedEvent;
import com.dev.jwt_login.identity.domain.event.UserCreatedEvent;
import com.dev.jwt_login.identity.domain.event.UserUpdatedEvent;
import com.dev.jwt_login.identity.domain.model.valueobject.Email;
import com.dev.jwt_login.identity.domain.model.valueobject.Password;
import com.dev.jwt_login.identity.domain.model.valueobject.UserId;
import com.dev.jwt_login.identity.domain.model.valueobject.Username;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class User {

    private final UserId id;
    private final Username username;
    private final LocalDateTime createdAt;
    @Getter(AccessLevel.NONE)
    private final Set<Role> roles;
    @Getter(AccessLevel.NONE)
    private final List<DomainEvent> events;
    private Email email;
    private Password password;
    private boolean enabled;
    private LocalDateTime updatedAt;

    public User(UserId id,
                Username username,
                Email email,
                Password password,
                boolean enabled,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                Set<Role> roles) {
        if (username == null) {
            throw new IllegalArgumentException("Username is required");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password is required");
        }
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
        this.roles = roles == null ? new HashSet<>() : new HashSet<>(roles);
        this.events = new ArrayList<>();
    }

    public static User create(UserId id,
                              Username username,
                              Email email,
                              Password password) {
        User user = new User(id, username, email, password, true, null, null, null);
        user.recordEvent(new UserCreatedEvent(id, username.value()));
        return user;
    }

    public static User create(Username username,
                              Email email,
                              Password password) {
        return create(null, username, email, password);
    }

    public void recordAuthentication() {
        recordEvent(new UserAuthenticatedEvent(id, username.value()));
    }

    public void changeEmail(Email newEmail) {
        if (newEmail == null) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!this.email.equals(newEmail)) {
            this.email = newEmail;
            markModified();
        }
    }

    public void changePassword(Password newPassword) {
        if (newPassword == null) {
            throw new IllegalArgumentException("Password is required");
        }
        if (!this.password.equals(newPassword)) {
            this.password = newPassword;
            markModified();
        }
    }

    public void setEnabled(Boolean enabled) {
        if (enabled == null) {
            return;
        }
        if (this.enabled != enabled) {
            this.enabled = enabled;
            markModified();
        }
    }

    public void grantRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }
        this.roles.add(role);
    }

    private void markModified() {
        this.updatedAt = LocalDateTime.now();
        recordEvent(new UserUpdatedEvent(id));
    }

    private void recordEvent(DomainEvent event) {
        this.events.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(events);
    }

    public void clearDomainEvents() {
        events.clear();
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }
}
