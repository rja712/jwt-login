package com.dev.jwt_login.identity.inbound.model.mapper;

import com.dev.jwt_login.identity.domain.model.entity.Role;
import com.dev.jwt_login.identity.domain.model.entity.User;
import com.dev.jwt_login.identity.domain.model.valueobject.Email;
import com.dev.jwt_login.identity.domain.model.valueobject.UserId;
import com.dev.jwt_login.identity.domain.model.valueobject.Username;
import com.dev.jwt_login.identity.inbound.model.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDomainMapper {

    UserResponse toDto(User user);

    default Long mapUserId(UserId userId) {
        return userId == null ? null : userId.value();
    }

    default String mapUsername(Username username) {
        return username == null ? null : username.value();
    }

    default String mapEmail(Email email) {
        return email == null ? null : email.value();
    }

    default String mapRole(Role role) {
        return role == null ? null : role.name().value();
    }
}
