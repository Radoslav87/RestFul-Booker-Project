package com.restfulbooker.steps.booking;

import com.restfulbooker.api.constants.BasePathsConstants;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.helper.CustomRequestSpecification;
import com.restfulbooker.api.helper.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class GetBookingSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetBookingSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification    = new CustomRequestSpecification();

        requestSpecification.addBasePath(BasePathsConstants.GET_BOOKING);
        requestSpecification.setContentType(ContentType.JSON);

    }

    @Step("Get booking by id")
    public BookingPayloadDTO getBookingById(int bookingId) {
        Response response = getBookingResponse(bookingId);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(BookingPayloadDTO.class);
    }

    @Step("Get booking (expect 404 Not Found)")
    public String getBookingNotFoundError(int bookingId) {
        Response response = getBookingResponse(bookingId);

        response.then().statusCode(HttpStatus.SC_NOT_FOUND);
        return response.asString();
    }

    private Response getBookingResponse(int bookingId) {

        requestSpecification.addPathParam("Id", bookingId);
        return requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
