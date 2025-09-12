package com.restfulbooker.api.dtos.booking;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateBookingResponseDTO {

    private int bookingid;
    private BookingPayloadDTO booking;

}
