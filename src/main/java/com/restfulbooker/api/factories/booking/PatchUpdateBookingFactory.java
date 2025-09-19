package com.restfulbooker.api.factories.booking;

import com.restfulbooker.api.dtos.booking.PatchBookingRequestDTO;

public class PatchUpdateBookingFactory {

    public PatchBookingRequestDTO patchBookingPayloadDTO(String firstname, String lastname) {
        return PatchBookingRequestDTO.builder()
                .firstname(firstname)
                .lastname(lastname)
                .build();
    }
}
