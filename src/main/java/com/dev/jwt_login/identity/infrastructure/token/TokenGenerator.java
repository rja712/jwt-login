package com.dev.jwt_login.identity.infrastructure.token;

import java.util.List;

public interface TokenGenerator {

    String generate(String username, List<String> roles);
}
