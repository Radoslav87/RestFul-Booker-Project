package com.restfulbooker.api.booking;

import com.restfulbooker.constants.ErrorMessageConstants;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.dtos.booking.CreateBookingResponseDTO;
import com.restfulbooker.helpers.InputDataHelper;
import com.restfulbooker.base.ApiBaseTest;
import com.restfulbooker.api.factories.booking.PostBookingFactory;
import com.restfulbooker.api.steps.authorization.CreateTokenSteps;
import com.restfulbooker.api.steps.booking.DeleteBookingSteps;
import com.restfulbooker.api.steps.booking.PostBookingSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Feature("Booking")
@Epic("Restful-Booker")
@Story("Delete Booking")
public class DeleteBookingTest extends ApiBaseTest {

    private InputDataHelper data;
    private PostBookingSteps postSteps;
    private DeleteBookingSteps deleteSteps;
    private CreateTokenSteps authSteps;

    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        data        = new InputDataHelper();
        postSteps   = new PostBookingSteps();
        deleteSteps = new DeleteBookingSteps();
        authSteps   = new CreateTokenSteps();

        token = authSteps.createToken(username(), password()).getToken();
    }

    private BookingPayloadDTO basePayload() {
        return new PostBookingFactory().createBookingPayloadDTO(
                data.bookingFirstname(),
                data.bookingLastname(),
                data.bookingTotalprice(),
                data.bookingDepositpaid(),
                data.bookingCheckin().toLocalDate(),
                data.bookingCheckout().toLocalDate(),
                data.bookingAdditionalneeds()
        );
    }

    @Test(description = "Delete booking successfully (expects 201 per API behavior)")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBookingTest() {
        CreateBookingResponseDTO created = postSteps.createBooking(basePayload());
        int bookingId = created.getBookingid();

        String body = deleteSteps.deleteBooking(token, bookingId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(body != null && !body.isBlank(), "Response body should not be blank");

        softAssert.assertAll();
    }

    @Test(description = "Delete booking with invalid token -> 403 Forbidden")
    @Severity(SeverityLevel.MINOR)
    public void deleteBookingForbiddenTest() {
        CreateBookingResponseDTO created = postSteps.createBooking(basePayload());
        int bookingId = created.getBookingid();

        String error = deleteSteps.deleteBookingForbiddenError("bad-token", bookingId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(error, ErrorMessageConstants.FORBIDDEN_ERROR, "Error body should be 'Forbidden'");

        softAssert.assertAll();
    }

    @Issue("RB-005")
    @Test(groups = {"BUG", "CONTRACT"}, description = "CONTRACT: Delete should return 200 OK (expected fail if API returns 201)")
    @Severity(SeverityLevel.NORMAL)
    public void deleteBookingOkContractTest() {

        CreateBookingResponseDTO created = postSteps.createBooking(basePayload());
        int bookingId = created.getBookingid();

        deleteSteps.deleteBookingOkContract(token, bookingId);
    }
}