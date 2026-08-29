package com.dev.jwt_login.identity.outbound.persistence.mapper;

import com.dev.jwt_login.identity.domain.model.entity.Permission;
import com.dev.jwt_login.identity.outbound.persistence.entity.PermissionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionEntityMapper {

    Permission toDomain(PermissionEntity entity);

    PermissionEntity toEntity(Permission domain);
}
