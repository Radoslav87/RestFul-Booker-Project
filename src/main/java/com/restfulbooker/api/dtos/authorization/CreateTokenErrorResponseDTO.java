package com.restfulbooker.api.dtos.authorization;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTokenErrorResponseDTO {

    private String reason;
}
