package com.dev.jwt_login.identity.outbound.persistence.port.impl;

import com.dev.jwt_login.identity.domain.model.entity.User;
import com.dev.jwt_login.identity.domain.model.valueobject.Username;
import com.dev.jwt_login.identity.domain.port.UserPersistencePort;
import com.dev.jwt_login.identity.outbound.persistence.entity.UserEntity;
import com.dev.jwt_login.identity.outbound.persistence.jpa.UserEntityRepository;
import com.dev.jwt_login.identity.outbound.persistence.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaUserPersistencePort implements UserPersistencePort {

    private final UserEntityRepository jpaRepository;
    private final UserEntityMapper userMapper;

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(Username username) {
        return jpaRepository.findByUsername(username.value()).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
