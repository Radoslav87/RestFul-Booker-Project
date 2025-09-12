package com.restfulbooker.api.dtos.authorization;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class CreateTokenRequestDTO {

    private String username;
    private String password;

}
