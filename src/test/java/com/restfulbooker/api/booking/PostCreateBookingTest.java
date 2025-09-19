package com.restfulbooker.api.booking;

import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.constants.ErrorMessageConstants;
import com.restfulbooker.api.dtos.booking.CreateBookingResponseDTO;
import com.restfulbooker.base.ApiBaseTest;
import com.restfulbooker.api.steps.booking.PostBookingSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


@Feature("Booking")
@Epic("Restful-Booker")
public class PostCreateBookingTest extends ApiBaseTest {


    private PostBookingSteps postSteps;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        postSteps = new PostBookingSteps();
    }

    @Test(description = "Create booking")
    @Severity(SeverityLevel.CRITICAL)
    public void createBookingTest() {
        CreateBookingResponseDTO response = postSteps.createBookingAndReturnFullResponse();
        BookingPayloadDTO booking = response.getBooking();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(response.getBookingid() > 0, "bookingid should be > 0");
        softAssert.assertEquals(booking.getFirstname(), data.bookingFirstname(), "firstname mismatch");
        softAssert.assertEquals(booking.getLastname(), data.bookingLastname(), "lastname mismatch");
        softAssert.assertEquals(booking.getTotalprice(), data.bookingTotalprice(), "totalprice mismatch");
        softAssert.assertEquals(booking.isDepositpaid(), data.bookingDepositpaid(), "depositpaid mismatch");
        softAssert.assertEquals(booking.getAdditionalneeds(), data.bookingAdditionalneeds(), "additionalneeds mismatch");

        softAssert.assertEquals(response.getBooking().getBookingdates().getCheckin().toString(), data.bookingCheckin().toLocalDate().toString(), "checkin mismatch");
        softAssert.assertEquals(response.getBooking().getBookingdates().getCheckout().toString(), data.bookingCheckout().toLocalDate().toString(), "checkout mismatch");

        softAssert.assertAll();
    }

    @Test(description = "Create booking with malformed JSON (missing field)")
    public void createBookingInvalidJsonTest() {
        String errorMessage = postSteps.createBookingInvalidJsonError();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorMessage, ErrorMessageConstants.BAD_REQUEST_ERROR, "Expected 'Bad Request'");

        softAssert.assertAll();
    }

    @Issue("RB-002")
    @Test(groups = {"BUG", "CONTRACT"}, description = "CONTRACT: Post with empty body must return 400 (actual: 500)")
    @Severity(SeverityLevel.MINOR)
    public void createBookingEmptyBodyTest() {
        String errorMessage = postSteps.createBookingEmptyBodyError();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorMessage, ErrorMessageConstants.BAD_REQUEST_ERROR, "Expected 'Bad Request'");

        softAssert.assertAll();
    }

    @Issue("RB-003")
    @Test(groups = {"BUG", "CONTRACT"},description = "CONTRACT: Post with invalid content type return 500 ")
    @Severity(SeverityLevel.MINOR)
    public void createBookingWrongContentTypeTest() {
        String errorMessage = postSteps.createBookingWrongContentTypeError();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorMessage, ErrorMessageConstants.UNSUPPORTED_MEDIA_TYPE, "Expected 'Unsupported Media Type'");

        softAssert.assertAll();
    }

}