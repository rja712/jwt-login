package com.dev.jwt_login.identity.domain.port;

import com.dev.jwt_login.identity.domain.model.entity.User;
import com.dev.jwt_login.identity.domain.model.valueobject.Username;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(Username username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAll();

    boolean existsById(Long id);

    User save(User user);

    void deleteById(Long id);
}
