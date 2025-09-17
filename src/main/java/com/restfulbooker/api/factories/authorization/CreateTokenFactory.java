package com.restfulbooker.api.factories.authorization;

import com.restfulbooker.api.dtos.authorization.CreateTokenRequestDTO;

public class CreateTokenFactory {

    public CreateTokenRequestDTO createTokenRequest(String username, String password) {
        return CreateTokenRequestDTO.builder()
                .username(username)
                .password(password)
                .build();
    }
}