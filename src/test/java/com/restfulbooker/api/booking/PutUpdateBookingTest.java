package com.restfulbooker.api.booking;

import com.restfulbooker.constants.ErrorMessageConstants;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.dtos.booking.CreateBookingResponseDTO;
import com.restfulbooker.base.ApiBaseTest;
import com.restfulbooker.api.steps.authorization.CreateTokenSteps;
import com.restfulbooker.api.steps.booking.PostBookingSteps;
import com.restfulbooker.api.steps.booking.PutUpdateBookingSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Feature("Booking")
@Epic("Restful-Booker")
@Story("Update Booking")
public class PutUpdateBookingTest extends ApiBaseTest {

    private PostBookingSteps postSteps;
    private PutUpdateBookingSteps putSteps;


    @BeforeClass(alwaysRun = true)
    public void setup() {
        postSteps = new PostBookingSteps();

        CreateTokenSteps auth = new CreateTokenSteps();
        String token = auth.createToken(username(), password()).getToken();

        putSteps = new PutUpdateBookingSteps(token);
    }

    @Test(description = "Update booking successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void updateBookingTest() {
        CreateBookingResponseDTO created = postSteps.createBooking(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());

        int bookingId = created.getBookingid();

        BookingPayloadDTO updated = putSteps.updateBooking(bookingId, data.bookingUpdFirstname(), data.bookingUpdLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(updated.getFirstname(), data.bookingUpdFirstname(), "firstname");
        softAssert.assertEquals(updated.getLastname(), data.bookingUpdLastname(), "lastname");
        softAssert.assertEquals(updated.getTotalprice(), data.bookingTotalprice(), "totalprice");
        softAssert.assertEquals(updated.isDepositpaid(), data.bookingDepositpaid(), "depositpaid");
        softAssert.assertEquals(updated.getAdditionalneeds(), data.bookingAdditionalneeds(), "additionalneeds");
        softAssert.assertEquals(updated.getBookingdates().getCheckin(), data.bookingCheckin().toLocalDate(), "checkin");
        softAssert.assertEquals(updated.getBookingdates().getCheckout(), data.bookingCheckout().toLocalDate(), "checkout");

        softAssert.assertAll();
    }

    @Issue("RB-004")
    @Test(groups = {"BUG", "CONTRACT"},description = "CONTRACT: Update booking with invalid token -> 403 Forbidden")
    @Severity(SeverityLevel.NORMAL)
    public void updateBookingInvalidTokenTest() {

        CreateBookingResponseDTO created = postSteps.createBooking(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());

        int bookingId = created.getBookingid();

        PutUpdateBookingSteps invalidTokenSteps = new PutUpdateBookingSteps("invalid-token");

        String errorMessage = invalidTokenSteps.updateBookingWithInvalidToken(bookingId, data.bookingUpdFirstname(), data.bookingUpdLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorMessage, ErrorMessageConstants.FORBIDDEN_ERROR, "Expected 'Forbidden'");

        softAssert.assertAll();
    }

}
