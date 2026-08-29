package com.dev.jwt_login.identity.outbound.event;

import com.dev.jwt_login.identity.domain.event.DomainEvent;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
