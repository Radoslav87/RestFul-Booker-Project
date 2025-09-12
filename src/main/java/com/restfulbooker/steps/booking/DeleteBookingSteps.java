package com.restfulbooker.steps.booking;

import com.restfulbooker.api.helper.CustomRequestSpecification;
import com.restfulbooker.api.helper.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.restfulbooker.api.constants.BasePathsConstants.DELETE_BOOKING;
import static org.apache.http.HttpStatus.*;

public class DeleteBookingSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public DeleteBookingSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification    = new CustomRequestSpecification();

        requestSpecification.addBasePath(DELETE_BOOKING);
        requestSpecification.setContentType(ContentType.JSON);
    }

    @Step("Delete booking")
    public String deleteBooking(String token, int bookingId) {
        Response response = deleteBookingResponse(token, bookingId);
        response.then().statusCode(SC_CREATED);
        return response.asString();
    }

    @Step("Delete booking id with invalid/missing token (expect 403)")
    public String deleteBookingForbiddenError(String badToken, int bookingId) {
        Response response = deleteBookingResponse(badToken, bookingId);
        response.then().statusCode(SC_FORBIDDEN);
        return response.asString();
    }

    @Step("CONTRACT: Delete booking id should return 200 OK")
    public void deleteBookingOkContract(String token, int bookingId) {
        deleteBookingResponse(token, bookingId)
                .then()
                .statusCode(SC_OK);
    }

    private Response deleteBookingResponse(String token, Object bookingIdOrAlpha) {
        requestSpecification.addPathParam("Id", bookingIdOrAlpha);
        requestSpecification.setTokenCookie(token);

        return requestOperationsHelper
                .sendDeleteRequest(requestSpecification.getFilterableRequestSpecification());
    }

}