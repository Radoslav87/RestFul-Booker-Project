package com.restfulbooker.factories.booking;

import com.restfulbooker.api.dtos.booking.PatchBookingRequestDTO;

public class PartialUpdateBookingFactory {

    public PatchBookingRequestDTO patchBookingPayloadDTO(String firstname, String lastname) {
        return PatchBookingRequestDTO.builder()
                .firstname(firstname)
                .lastname(lastname)
                .build();
    }
}
