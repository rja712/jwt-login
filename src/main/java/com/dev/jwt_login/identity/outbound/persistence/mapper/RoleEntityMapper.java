package com.dev.jwt_login.identity.outbound.persistence.mapper;

import com.dev.jwt_login.identity.domain.model.entity.Role;
import com.dev.jwt_login.identity.domain.model.valueobject.RoleName;
import com.dev.jwt_login.identity.outbound.persistence.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PermissionEntityMapper.class)
public interface RoleEntityMapper {

    Role toDomain(RoleEntity entity);

    RoleEntity toEntity(Role domain);

    default RoleName mapRoleName(String value) {
        return value == null ? null : new RoleName(value);
    }

    default String mapRoleName(RoleName roleName) {
        return roleName == null ? null : roleName.value();
    }
}
