package com.dev.jwt_login.identity.domain.port;

import com.dev.jwt_login.identity.domain.model.entity.Role;
import com.dev.jwt_login.identity.domain.model.valueobject.RoleName;

import java.util.Optional;

public interface RolePersistencePort {

    Optional<Role> findByName(RoleName name);
}
