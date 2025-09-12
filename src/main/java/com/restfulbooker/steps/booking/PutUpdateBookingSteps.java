package com.restfulbooker.steps.booking;

import com.restfulbooker.api.constants.BasePathsConstants;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.helper.CustomRequestSpecification;
import com.restfulbooker.api.helper.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class PutUpdateBookingSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PutUpdateBookingSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification    = new CustomRequestSpecification();

        requestSpecification.addBasePath(BasePathsConstants.PUT_BOOKING);   // "/booking/{Id}"
        requestSpecification.setContentType(ContentType.JSON);

        requestSpecification.addCustomHeader("Cookie", "token=" + token);
    }

    @Step("Update booking id")
    public BookingPayloadDTO updateBooking(int bookingId, BookingPayloadDTO body) {
        Response res = updateBookingResponse(bookingId, body);
        res.then().statusCode(HttpStatus.SC_OK);
        return res.as(BookingPayloadDTO.class);
    }

    @Step("Update booking id with invalid/absent token (expect 403)")
    public String updateBookingForbiddenError(int bookingId, BookingPayloadDTO body) {
        Response res = updateBookingResponse(bookingId, body);
        res.then().statusCode(HttpStatus.SC_FORBIDDEN);
        return res.asString();
    }

    private Response updateBookingResponse(int bookingId, BookingPayloadDTO body) {
        Map<String, Object> path = new HashMap<>();
        path.put("Id", bookingId);

        requestSpecification.addPathParams(path);
        requestSpecification.addBodyToRequest(body);

        return requestOperationsHelper
                .sendPutRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
