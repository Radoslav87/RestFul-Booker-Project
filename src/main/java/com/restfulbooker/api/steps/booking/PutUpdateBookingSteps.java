package com.restfulbooker.api.steps.booking;

import com.restfulbooker.api.factories.booking.PostBookingPayloadFactory;
import com.restfulbooker.constants.BasePathsConstants;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.helpers.CustomRequestSpecification;
import com.restfulbooker.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.restfulbooker.constants.BasePathsConstants.PUT_BOOKING;

public class PutUpdateBookingSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;
    private final PostBookingPayloadFactory payloadFactory;

    public PutUpdateBookingSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification    = new CustomRequestSpecification();
        payloadFactory = new PostBookingPayloadFactory();

        requestSpecification.addBasePath(PUT_BOOKING);
        requestSpecification.setContentType(ContentType.JSON);

        requestSpecification.addCustomHeader("Cookie", "token=" + token);
    }

    @Step("Update booking with valid token")
    public BookingPayloadDTO updateBooking(int bookingId, String firstname, String lastname, int totalprice, boolean depositpaid, LocalDate checkin, LocalDate checkout, String additionalneeds) {

        BookingPayloadDTO body = payloadFactory.createBookingPayloadDTO(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);

        Response response = createUpdateBookingResponse(bookingId, body);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(BookingPayloadDTO.class);
    }

    @Step("Update booking with invalid/absent token (expect 403 Forbidden)")
    public String updateBookingWithInvalidToken(int bookingId, String firstname, String lastname, int totalprice, boolean depositpaid, LocalDate checkin, LocalDate checkout, String additionalneeds) {

        BookingPayloadDTO body = payloadFactory.createBookingPayloadDTO(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);

        Response response = createUpdateBookingResponse(bookingId, body);

        response.then().statusCode(HttpStatus.SC_FORBIDDEN);

        return response.asString();
    }

    private Response createUpdateBookingResponse(int bookingId, BookingPayloadDTO body) {
        Map<String, Object> path = new HashMap<>();
        path.put("Id", bookingId);

        requestSpecification.addPathParams(path);
        requestSpecification.addBodyToRequest(body);

        return requestOperationsHelper
                .sendPutRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
