package com.restfulbooker.api.authorization;

import com.restfulbooker.api.dtos.authorization.CreateTokenErrorResponseDTO;
import com.restfulbooker.api.dtos.authorization.CreateTokenResponseDTO;
import com.restfulbooker.base.ApiBaseTest;
import com.restfulbooker.api.steps.authorization.CreateTokenSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.restfulbooker.constants.ErrorMessageConstants.BAD_CREDENTIALS;

@Epic("Restful-Booker")
@Feature("Authorization")
public class PostCreateTokenTest extends ApiBaseTest {

    private CreateTokenSteps steps;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        steps= new CreateTokenSteps();
    }

    @Test(description = "Create token with valid credentials")
    @Story("Create Token")
    @Severity(SeverityLevel.CRITICAL)
    public void createTokenTest() {

        CreateTokenResponseDTO createTokenResponseDTO = steps.createToken(username(),password());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(createTokenResponseDTO.getToken(), "Token should not be null");
        softAssert.assertFalse(createTokenResponseDTO.getToken().isBlank(), "Token should not be blank");

        softAssert.assertAll();
    }

    @Test(description = "Create token with invalid credentials → CURRENT behavior: 200 + error body")
    @Story("Create Token")
    @Severity(SeverityLevel.NORMAL)
    public void createTokenInvalidCredentialsTest() {

        CreateTokenErrorResponseDTO errorResponseDTO =steps.createTokenInvalidCredentials(username(), data.invalidPassword());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDTO.getReason(), BAD_CREDENTIALS, "'reason' should be 'Bad credentials'");
        softAssert.assertFalse(errorResponseDTO.getReason().isBlank(), "'reason' should not be blank");

        softAssert.assertAll();
    }

    @Issue("RB-001")
    @Test(groups = {"BUG", "CONTRACT"},description = "CONTRACT: Invalid credentials must return 401 ")
    @Severity(SeverityLevel.NORMAL)
    public void createTokenUnauthorizedContractTest() {

        CreateTokenErrorResponseDTO errorResponseStatusCode = steps.createTokenUnauthorizedContract(username(), data.invalidPassword());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseStatusCode.getReason(), BAD_CREDENTIALS, "Error 'reason' should be 'Bad credentials'");
        softAssert.assertFalse(errorResponseStatusCode.getReason().isBlank(), "'reason' should not be blank");

        softAssert.assertAll();
    }
}