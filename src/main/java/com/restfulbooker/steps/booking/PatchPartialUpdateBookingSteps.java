package com.restfulbooker.steps.booking;

import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.dtos.booking.PatchBookingRequestDTO;
import com.restfulbooker.api.helper.CustomRequestSpecification;
import com.restfulbooker.api.helper.RequestOperationsHelper;
import com.restfulbooker.factories.booking.PartialUpdateBookingFactory;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.restfulbooker.api.constants.BasePathsConstants.PATCH_BOOKING;

public class PatchPartialUpdateBookingSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PatchPartialUpdateBookingSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(PATCH_BOOKING);
        requestSpecification.setContentType(ContentType.JSON);
    }


    @Step("Partial update booking #{bookingId} (firstname/lastname)")
    public BookingPayloadDTO patchBookingNames(String token, int bookingId, String firstname, String lastname) {
        PatchBookingRequestDTO body = new PartialUpdateBookingFactory()
                .patchBookingPayloadDTO(firstname, lastname);

        Response response = sendPatchPartialBookingRequest(bookingId, token, body);
        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(BookingPayloadDTO.class);
    }

    @Step("Partial update booking #{bookingId} (firstname/lastname) with invalid/missing token (expect 403)")
    public String patchBookingNamesForbiddenError(int bookingId, String badToken, String firstname, String lastname) {
        PatchBookingRequestDTO body = new PartialUpdateBookingFactory()
                .patchBookingPayloadDTO(firstname, lastname);

        Response response = sendPatchPartialBookingRequest(bookingId, badToken, body);
        response.then().statusCode(HttpStatus.SC_FORBIDDEN);
        return response.asString();
    }

    private Response sendPatchPartialBookingRequest(int bookingId, String token, Object body) {
        requestSpecification.addPathParam("Id", bookingId);
        requestSpecification.setTokenCookie(token);

        requestSpecification.addBodyToRequest(body);
        return requestOperationsHelper
                .sendPatchRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
