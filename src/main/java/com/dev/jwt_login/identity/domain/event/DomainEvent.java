package com.dev.jwt_login.identity.domain.event;

import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class DomainEvent {

    private final Instant occurredAt;

    protected DomainEvent() {
        this.occurredAt = Instant.now();
    }
}
