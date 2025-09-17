package com.restfulbooker.api.factories.booking;

import com.restfulbooker.api.dtos.booking.BookingDatesDTO;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;

import java.time.LocalDate;

public class PostBookingFactory {

    public BookingPayloadDTO createBookingPayloadDTO(String firstname, String lastname, int totalprice, boolean depositpaid, LocalDate checkin, LocalDate checkout, String additionalneeds) {
        BookingDatesDTO dates = BookingDatesDTO.builder()
                .checkin(checkin)
                .checkout(checkout)
                .build();

        return BookingPayloadDTO.builder()
                .firstname(firstname)
                .lastname(lastname)
                .totalprice(totalprice)
                .depositpaid(depositpaid)
                .bookingdates(dates)
                .additionalneeds(additionalneeds)
                .build();
    }
}

