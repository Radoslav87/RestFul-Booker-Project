package com.restfulbooker.api.booking;

import com.restfulbooker.api.constants.ErrorMessageConstants;
import com.restfulbooker.base.ApiBaseTest;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.dtos.booking.CreateBookingResponseDTO;
import com.restfulbooker.factories.booking.PostBookingFactory;
import com.restfulbooker.steps.booking.GetBookingSteps;
import com.restfulbooker.steps.booking.PostBookingSteps;
import com.restfulbooker.api.helper.InputDataHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDate;

@Feature("Booking")
@Epic("Restful-Booker")
@Story("Get Booking")
public class GetBookingTest extends ApiBaseTest {

    private PostBookingSteps postSteps;
    private GetBookingSteps getSteps;
    private InputDataHelper data;

    private int bookingId;
    private BookingPayloadDTO expectedPayload;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        postSteps = new PostBookingSteps();
        getSteps  = new GetBookingSteps();
        data      = new InputDataHelper();

        LocalDate checkin  = data.bookingCheckin().toLocalDate();
        LocalDate checkout = data.bookingCheckout().toLocalDate();

        expectedPayload = new PostBookingFactory().createBookingPayloadDTO(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), checkin, checkout, data.bookingAdditionalneeds());
        CreateBookingResponseDTO created = postSteps.createBooking(expectedPayload);
        bookingId = created.getBookingid();
    }

    @Test(description = "Get booking by id")
    @Description("Successful get booking by id (200)")
    @Severity(SeverityLevel.NORMAL)
    public void getBookingTest() {
        BookingPayloadDTO actual = getSteps.getBookingById(bookingId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(actual.getFirstname(),expectedPayload.getFirstname(),"firstname mismatch");
        softAssert.assertEquals(actual.getLastname(),expectedPayload.getLastname(), "lastname mismatch");
        softAssert.assertEquals(actual.getTotalprice(),expectedPayload.getTotalprice(),"totalprice mismatch");
        softAssert.assertEquals(actual.isDepositpaid(),expectedPayload.isDepositpaid(),"depositpaid mismatch");
        softAssert.assertEquals(actual.getAdditionalneeds(),expectedPayload.getAdditionalneeds(),"additionalneeds mismatch");
        softAssert.assertEquals(actual.getBookingdates().getCheckin(),  expectedPayload.getBookingdates().getCheckin(),  "checkin mismatch");
        softAssert.assertEquals(actual.getBookingdates().getCheckout(), expectedPayload.getBookingdates().getCheckout(), "checkout mismatch");

        softAssert.assertAll();
    }

    @Test(description = "Get booking with not existing id")
    @Description("Get booking returns 404 Not Found for missing id")
    @Severity(SeverityLevel.MINOR)
    public void getBookingNotExistingIdTest() {
        int notExistingId = 999_999_999;

        String body = getSteps.getBookingNotFoundError(notExistingId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(body, ErrorMessageConstants.NOT_FOUND_ERROR,"Error body should be 'Not Found'");

        softAssert.assertAll();
    }
}