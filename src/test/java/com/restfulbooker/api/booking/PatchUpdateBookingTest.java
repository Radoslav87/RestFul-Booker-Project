package com.restfulbooker.api.booking;

import com.restfulbooker.constants.ErrorMessageConstants;
import com.restfulbooker.api.dtos.booking.BookingPayloadDTO;
import com.restfulbooker.api.dtos.booking.CreateBookingResponseDTO;
import com.restfulbooker.base.ApiBaseTest;
import com.restfulbooker.api.steps.authorization.CreateTokenSteps;
import com.restfulbooker.api.steps.booking.PatchPartialUpdateBookingSteps;
import com.restfulbooker.api.steps.booking.PostBookingSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Feature("Booking")
@Epic("Restful-Booker")
@Story("Partial Update Booking")
public class PatchUpdateBookingTest extends ApiBaseTest {

    private PostBookingSteps postSteps;
    private PatchPartialUpdateBookingSteps patchSteps;
    private CreateTokenSteps authSteps;

    private int bookingId;
    private BookingPayloadDTO original;
    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        postSteps  = new PostBookingSteps();
        patchSteps = new PatchPartialUpdateBookingSteps();
        authSteps  = new CreateTokenSteps();

        CreateBookingResponseDTO created = postSteps.createBooking(data.bookingFirstname(), data.bookingLastname(), data.bookingTotalprice(), data.bookingDepositpaid(), data.bookingCheckin().toLocalDate(), data.bookingCheckout().toLocalDate(), data.bookingAdditionalneeds());

        bookingId = created.getBookingid();
        original = created.getBooking();
        token = authSteps.createToken(username(), password()).getToken();
    }

    @Test(description = "PATCH names with valid token -> 200 and only names changed")
    @Severity(SeverityLevel.CRITICAL)
    public void patchBookingNames() {
        BookingPayloadDTO patched = patchSteps.patchBookingNames(token, bookingId, data.bookingFirstnamePatch(), data.bookingLastnamePatch());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(patched.getFirstname(), data.bookingFirstnamePatch(), "firstname");
        softAssert.assertEquals(patched.getLastname(), data.bookingLastnamePatch(), "lastname");
        softAssert.assertEquals(patched.getTotalprice(), original.getTotalprice(), "totalprice");
        softAssert.assertEquals(patched.isDepositpaid(), original.isDepositpaid(), "depositpaid");
        softAssert.assertEquals(patched.getAdditionalneeds(), original.getAdditionalneeds(), "additionalneeds");
        softAssert.assertEquals(patched.getBookingdates().getCheckin(), original.getBookingdates().getCheckin(), "checkin");
        softAssert.assertEquals(patched.getBookingdates().getCheckout(), original.getBookingdates().getCheckout(), "checkout");

        softAssert.assertAll();
    }

    @DataProvider(name = "badTokens")
    public Object[][] badTokens() {
        return new Object[][]{
                { null },
                { "invalidToken" }};
    }

    @Test(description = "PATCH names without/invalid token -> 403", dataProvider = "badTokens")
    @Severity(SeverityLevel.MINOR)
    public void partialUpdateWithoutValidTokenTest(String badToken) {
        String body = patchSteps.patchBookingNamesForbiddenError(bookingId, badToken, data.bookingFirstnamePatch(), data.bookingLastnamePatch());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(body, ErrorMessageConstants.FORBIDDEN_ERROR, "Error body should be 'Forbidden'");

        softAssert.assertAll();
    }
}