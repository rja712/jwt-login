package com.dev.jwt_login.identity.domain.event;

import com.dev.jwt_login.identity.domain.model.valueobject.UserId;
import lombok.Getter;

@Getter
public class UserAuthenticatedEvent extends DomainEvent {

    private final UserId userId;
    private final String username;

    public UserAuthenticatedEvent(UserId userId, String username) {
        super();
        this.userId = userId;
        this.username = username;
    }
}
