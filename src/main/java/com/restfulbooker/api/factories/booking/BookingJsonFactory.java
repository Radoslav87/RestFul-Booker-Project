package com.restfulbooker.api.factories.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;

import java.util.HashMap;
import java.util.Map;

public class BookingJsonFactory {

    private final ObjectMapper mapper;

    public BookingJsonFactory() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // 👉 ТОВА Е НАЙ-ВАЖНОТО!
    }

    public String validJsonString(BookingPayloadDTO dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize booking payload", e);
        }
    }


    public String malformedJsonMissingDepositPaid(BookingPayloadDTO dto) {
        return String.format("""
            {
              "firstname": "%s",
              "lastname": "%s",
              "totalprice": %d,
              "depositpaid": ,
              "bookingdates": {
                "checkin": "%s",
                "checkout": "%s"
              },
              "additionalneeds": "%s"
            }
            """,
                escape(dto.getFirstname()),
                escape(dto.getLastname()),
                dto.getTotalprice(),
                dto.getBookingdates().getCheckin(),
                dto.getBookingdates().getCheckout(),
                escape(dto.getAdditionalneeds())
        );
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}
