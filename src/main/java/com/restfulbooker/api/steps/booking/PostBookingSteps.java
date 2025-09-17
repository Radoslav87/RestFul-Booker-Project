package com.restfulbooker.api.steps.booking;

import com.restfulbooker.constants.BasePathsConstants;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.dtos.booking.CreateBookingResponseDTO;
import com.restfulbooker.helpers.CustomRequestSpecification;
import com.restfulbooker.helpers.RequestOperationsHelper;
import com.restfulbooker.utils.ResourceUtils;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class PostBookingSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostBookingSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(BasePathsConstants.POST_BOOKING);
        requestSpecification.setContentType(ContentType.JSON);
    }

    @Step("Create booking")
    public CreateBookingResponseDTO createBooking(BookingPayloadDTO payload) {
        Response response = createBookingResponse(payload);
        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(CreateBookingResponseDTO.class);
    }

    @Step("Create booking with invalid JSON")
    public String createBookingBadJsonError(String resourcePath) {
        String json = ResourceUtils.readClasspathResource(resourcePath);
        requestSpecification.addBodyToRequest(json);

        Response response = requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
        return response.asString();
    }

    @Step("Create booking with Content-Type text/plain")
    public String createBookingWrongContentTypeError(String resourcePath) {
        String json = ResourceUtils.readClasspathResource(resourcePath);

        requestSpecification.setContentType(ContentType.TEXT);
        requestSpecification.addBodyToRequest(json);

        Response response = requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());

        requestSpecification.setContentType(ContentType.JSON);

        response.then().statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
        return response.asString();
    }

    @Step("Create booking with EMPTY body")
    public String createBookingEmptyBodyError() {
        requestSpecification.addBodyToRequest("");

        Response response = requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
        return response.asString();
    }

    private Response createBookingResponse(BookingPayloadDTO payload) {
        requestSpecification.addBodyToRequest(payload);
        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }

}