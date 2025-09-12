package com.restfulbooker.api.booking;

import com.restfulbooker.api.constants.ErrorMessageConstants;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.dtos.booking.CreateBookingResponseDTO;
import com.restfulbooker.base.ApiBaseTest;
import com.restfulbooker.factories.booking.PostBookingFactory;
import com.restfulbooker.steps.authorization.CreateTokenSteps;
import com.restfulbooker.steps.booking.PostBookingSteps;
import com.restfulbooker.steps.booking.PutUpdateBookingSteps;
import com.restfulbooker.api.helper.InputDataHelper;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Feature("Booking")
@Epic("Restful-Booker")
@Story("Update Booking")
public class PutUpdateBookingTest extends ApiBaseTest {

    private InputDataHelper data;
    private PostBookingSteps postSteps;
    private PutUpdateBookingSteps putSteps;
    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        data = new InputDataHelper();
        postSteps = new PostBookingSteps();

        CreateTokenSteps auth = new CreateTokenSteps();
        token = auth.createToken(username(), password()).getToken();
    }

    @Test(description = "Update booking successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void updateBookingTest() {
        BookingPayloadDTO initial = new PostBookingFactory().createBookingPayloadDTO(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(),data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());
        CreateBookingResponseDTO created = postSteps.createBooking(initial);
        int bookingId = created.getBookingid();

        BookingPayloadDTO updateBody = new PostBookingFactory().createBookingPayloadDTO(data.bookingUpdFirstname(), data.bookingUpdLastname(), data.bookingUpdTotalprice(), data.bookingUpdDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingUpdAdditionalneeds());

        PutUpdateBookingSteps putSteps = new PutUpdateBookingSteps(token);
        BookingPayloadDTO updated = putSteps.updateBooking(bookingId, updateBody);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(updated.getFirstname(), updateBody.getFirstname(), "firstname");
        softAssert.assertEquals(updated.getLastname(),  updateBody.getLastname(),  "lastname");
        softAssert.assertEquals(updated.getTotalprice(), updateBody.getTotalprice(), "totalprice");
        softAssert.assertEquals(updated.isDepositpaid(), updateBody.isDepositpaid(), "depositpaid");
        softAssert.assertEquals(updated.getAdditionalneeds(), updateBody.getAdditionalneeds(), "additionalneeds");
        softAssert.assertEquals(updated.getBookingdates().getCheckin(),  updateBody.getBookingdates().getCheckin(),  "checkin");
        softAssert.assertEquals(updated.getBookingdates().getCheckout(), updateBody.getBookingdates().getCheckout(), "checkout");

        softAssert.assertAll();
    }

    @Issue("RB-004")
    @Test(groups = {"BUG", "CONTRACT"},description = "CONTRACT: Update booking with invalid token -> 403 Forbidden")
    @Severity(SeverityLevel.NORMAL)
    public void updateBookingInvalidTokenTest() {
        BookingPayloadDTO initial = new PostBookingFactory().createBookingPayloadDTO(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());
        CreateBookingResponseDTO created = postSteps.createBooking(initial);
        int bookingId = created.getBookingid();

        BookingPayloadDTO updateBody = new PostBookingFactory().createBookingPayloadDTO(data.bookingUpdFirstname(), data.bookingUpdLastname(), data.bookingUpdTotalprice(), data.bookingUpdDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingUpdAdditionalneeds());

        putSteps = new PutUpdateBookingSteps("bad-token");
        String errorMessage = putSteps.updateBookingForbiddenError(bookingId, updateBody);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorMessage, ErrorMessageConstants.FORBIDDEN_ERROR, "Error body should be 'Forbidden'");
        softAssert.assertAll();
    }

}
