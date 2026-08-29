package com.dev.jwt_login.identity.domain.event;

import com.dev.jwt_login.identity.domain.model.valueobject.UserId;
import lombok.Getter;

@Getter
public class UserUpdatedEvent extends DomainEvent {

    private final UserId userId;

    public UserUpdatedEvent(UserId userId) {
        super();
        this.userId = userId;
    }
}
