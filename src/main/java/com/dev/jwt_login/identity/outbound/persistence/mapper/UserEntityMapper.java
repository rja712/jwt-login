package com.dev.jwt_login.identity.outbound.persistence.mapper;

import com.dev.jwt_login.identity.domain.model.entity.User;
import com.dev.jwt_login.identity.domain.model.valueobject.Email;
import com.dev.jwt_login.identity.domain.model.valueobject.Password;
import com.dev.jwt_login.identity.domain.model.valueobject.UserId;
import com.dev.jwt_login.identity.domain.model.valueobject.Username;
import com.dev.jwt_login.identity.outbound.persistence.entity.UserEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = RoleEntityMapper.class)
public interface UserEntityMapper {

    User toDomain(UserEntity entity);

    @BeanMapping(ignoreUnmappedSourceProperties = "domainEvents")
    UserEntity toEntity(User domain);

    default UserId mapUserId(Long value) {
        return value == null ? null : new UserId(value);
    }

    default Long mapUserId(UserId userId) {
        return userId == null ? null : userId.value();
    }

    default Username mapUsername(String value) {
        return value == null ? null : new Username(value);
    }

    default String mapUsername(Username username) {
        return username == null ? null : username.value();
    }

    default Email mapEmail(String value) {
        return value == null ? null : new Email(value);
    }

    default String mapEmail(Email email) {
        return email == null ? null : email.value();
    }

    default Password mapPassword(String encodedValue) {
        return encodedValue == null ? null : Password.fromEncoded(encodedValue);
    }

    default String mapPassword(Password password) {
        return password == null ? null : password.getEncodedValue();
    }
}
