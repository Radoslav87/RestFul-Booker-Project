package com.restfulbooker.api.factories.booking;

import com.restfulbooker.api.dtos.booking.BookingDatesDTO;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;

import java.time.LocalDate;

public class PostBookingPayloadFactory {

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

    public BookingPayloadDTO createPayloadMissingField(BookingPayloadDTO baseDto) {

        return BookingPayloadDTO.builder()
                .firstname(baseDto.getFirstname())
                .lastname(baseDto.getLastname())
                .totalprice(baseDto.getTotalprice())
                .bookingdates(baseDto.getBookingdates())
                .additionalneeds(baseDto.getAdditionalneeds())
                .build(); // intentionally missing depositpaid
    }
}

