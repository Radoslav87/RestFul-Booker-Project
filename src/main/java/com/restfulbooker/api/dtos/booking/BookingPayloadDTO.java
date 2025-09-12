package com.restfulbooker.api.dtos.booking;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class BookingPayloadDTO {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDatesDTO bookingdates;
    private String additionalneeds;
}
