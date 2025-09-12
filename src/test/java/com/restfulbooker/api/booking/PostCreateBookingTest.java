package com.restfulbooker.api.booking;

import com.restfulbooker.TestDataPathConstants.TestDataPathConstants;
import com.restfulbooker.api.constants.ErrorMessageConstants;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.dtos.booking.CreateBookingResponseDTO;
import com.restfulbooker.api.helper.InputDataHelper;
import com.restfulbooker.base.ApiBaseTest;
import com.restfulbooker.factories.booking.PostBookingFactory;
import com.restfulbooker.steps.booking.PostBookingSteps;
import io.qameta.allure.*;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


@Feature("Booking")
@Epic("Restful-Booker")
@Story("Create Booking")
public class PostCreateBookingTest extends ApiBaseTest {

    private PostBookingSteps steps;
    private InputDataHelper data;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        steps = new PostBookingSteps();
        data = new InputDataHelper();
    }

    @Test(description = "Create booking")
    @Severity(SeverityLevel.CRITICAL)
    public void createBookingTest() {

        BookingPayloadDTO payload = new PostBookingFactory().createBookingPayloadDTO(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());

        CreateBookingResponseDTO response = steps.createBooking(payload);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(response.getBookingid() > 0, "bookingid should be > 0");
        softAssert.assertEquals(response.getBooking().getFirstname(), payload.getFirstname(), "firstname mismatch");
        softAssert.assertEquals(response.getBooking().getLastname(), payload.getLastname(), "lastname mismatch");
        softAssert.assertEquals(response.getBooking().getTotalprice(), payload.getTotalprice(), "totalprice mismatch");
        softAssert.assertEquals(response.getBooking().isDepositpaid(), payload.isDepositpaid(), "depositpaid mismatch");
        softAssert.assertEquals(response.getBooking().getAdditionalneeds(), payload.getAdditionalneeds(), "additionalneeds mismatch");
        softAssert.assertEquals(response.getBooking().getBookingdates().getCheckin(), payload.getBookingdates().getCheckin(), "checkin mismatch");
        softAssert.assertEquals(response.getBooking().getBookingdates().getCheckout(), payload.getBookingdates().getCheckout(), "checkout mismatch");

        softAssert.assertAll();
    }

    @Test(description = "Create booking with malformed JSON ")
    public void createBookingInvalidJsonTest() {

        String errorMessage = steps.createBookingBadJsonError(TestDataPathConstants.BOOKING_MISSING_PARAMETER);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorMessage, ErrorMessageConstants.BAD_REQUEST_ERROR, "Error body should be 'Bad Request'");

        softAssert.assertAll();

    }

    @Issue("RB-002")
    @Test(groups = {"BUG", "CONTRACT"},description = "CONTRACT: Post with empty body return 500")
    @Severity(SeverityLevel.MINOR)
    public void createBookingEmptyBodyTest() {
        String body = steps.createBookingEmptyBodyError();
        org.testng.Assert.assertEquals(body, ErrorMessageConstants.BAD_REQUEST_ERROR);
    }

    @Issue("RB-003")
    @Test(groups = {"BUG", "CONTRACT"},description = "CONTRACT: Post with invalid content type return 500 ")
    @Severity(SeverityLevel.MINOR)
    public void createBookingWrongContentTypeTest() {
        steps.createBookingWrongContentTypeError(TestDataPathConstants.BOOKING_MISSING_PARAMETER);
    }

}