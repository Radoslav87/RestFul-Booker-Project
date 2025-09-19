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

import static com.restfulbooker.constants.BasePathsConstants.GET_BOOKING;

public class GetBookingSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetBookingSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification    = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_BOOKING);
        requestSpecification.setContentType(ContentType.JSON);

    }

    @Step("Get booking by ID")
    public BookingPayloadDTO getBookingById(int bookingId) {
        Response response = getBookingResponse(bookingId);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(BookingPayloadDTO.class);
    }

    @Step("Get booking with not existing ID (expect 404)")
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
