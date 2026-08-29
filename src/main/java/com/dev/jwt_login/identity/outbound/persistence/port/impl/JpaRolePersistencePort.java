package com.dev.jwt_login.identity.outbound.persistence.port.impl;

import com.dev.jwt_login.identity.domain.model.entity.Role;
import com.dev.jwt_login.identity.domain.model.valueobject.RoleName;
import com.dev.jwt_login.identity.domain.port.RolePersistencePort;
import com.dev.jwt_login.identity.outbound.persistence.jpa.RoleEntityRepository;
import com.dev.jwt_login.identity.outbound.persistence.mapper.RoleEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaRolePersistencePort implements RolePersistencePort {

    private final RoleEntityRepository jpaRepository;
    private final RoleEntityMapper roleMapper;

    @Override
    public Optional<Role> findByName(RoleName name) {
        return jpaRepository.findByName(name.value()).map(roleMapper::toDomain);
    }
}
