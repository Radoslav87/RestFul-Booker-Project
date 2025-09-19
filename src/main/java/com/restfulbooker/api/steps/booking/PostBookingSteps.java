package com.restfulbooker.api.steps.booking;

import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.dtos.booking.CreateBookingResponseDTO;
import com.restfulbooker.api.factories.booking.BookingJsonFactory;
import com.restfulbooker.api.factories.booking.PostBookingPayloadFactory;
import com.restfulbooker.helpers.CustomRequestSpecification;
import com.restfulbooker.helpers.InputDataHelper;
import com.restfulbooker.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.time.LocalDate;

import static com.restfulbooker.constants.BasePathsConstants.POST_BOOKING;

public class PostBookingSteps {

    private final InputDataHelper data;
    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;
    private final PostBookingPayloadFactory payloadFactory;
    private final BookingJsonFactory jsonFactory;

    public PostBookingSteps() {
        data = new InputDataHelper();
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();
        payloadFactory = new PostBookingPayloadFactory();
        jsonFactory = new BookingJsonFactory();

        requestSpecification.addBasePath(POST_BOOKING);
        requestSpecification.setContentType(ContentType.JSON);
    }

    @Step("Create booking with valid data")
    public CreateBookingResponseDTO createBooking(String firstname, String lastname, int totalprice, boolean depositpaid, LocalDate checkin, LocalDate checkout, String additionalneeds) {

        BookingPayloadDTO payload = payloadFactory.createBookingPayloadDTO(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);

        return createBookingRequest(payload, HttpStatus.SC_OK)
                .as(CreateBookingResponseDTO.class);
    }

    @Step("Create booking and return full response")
    public CreateBookingResponseDTO createBookingAndReturnFullResponse() {
        return createBooking(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());
    }

    @Step("Create booking and return ID only")
    public int createBookingAndReturnId() {
        return createBooking(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds())
                .getBookingid();
    }

    @Step("Create booking with provided DTO")
    public CreateBookingResponseDTO createBookingWithDTO(BookingPayloadDTO payload) {

        return createBookingRequest(payload, HttpStatus.SC_OK)
                .as(CreateBookingResponseDTO.class);
    }

    @Step("Create booking with invalid JSON")
    public String createBookingInvalidJsonError() {
        BookingPayloadDTO fullPayload = payloadFactory.createBookingPayloadDTO(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());

        String brokenJson = jsonFactory.malformedJsonMissingDepositPaid(fullPayload);

        return createBookingRequest(brokenJson, HttpStatus.SC_BAD_REQUEST)
                .asString();
    }

    @Step("Create booking with Content-Type text/plain")
    public String createBookingWrongContentTypeError() {
        BookingPayloadDTO payload = payloadFactory.createBookingPayloadDTO(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());

        String validJson = jsonFactory.validJsonString(payload);

        requestSpecification.setContentType(ContentType.TEXT);
        String response = createBookingRequest(validJson, HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE).asString();

        requestSpecification.setContentType(ContentType.JSON);
        return response;
    }

    @Step("Create booking with EMPTY body (expect 400)")
    public String createBookingEmptyBodyError() {
        return createBookingRequest("", HttpStatus.SC_BAD_REQUEST)
                .asString();
    }

    private Response createBookingRequest(Object body, int expectedStatusCode) {
        requestSpecification.addBodyToRequest(body);

        Response response = requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(expectedStatusCode);
        return response;
    }
}