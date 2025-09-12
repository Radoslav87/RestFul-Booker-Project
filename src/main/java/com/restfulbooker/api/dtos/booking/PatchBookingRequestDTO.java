package com.restfulbooker.api.dtos.booking;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchBookingRequestDTO {
    private String firstname;
    private String lastname;
}
