package com.restfulbooker.api.authorization;

import com.restfulbooker.api.constants.ErrorMessageConstants;
import com.restfulbooker.api.dtos.authorization.CreateTokenErrorResponseDTO;
import com.restfulbooker.api.dtos.authorization.CreateTokenResponseDTO;
import com.restfulbooker.api.helper.InputDataHelper;
import com.restfulbooker.base.ApiBaseTest;
import com.restfulbooker.steps.authorization.CreateTokenSteps;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.restfulbooker.api.constants.ErrorMessageConstants.BAD_CREDENTIALS;

@Feature("Authorization")
public class PostCreateTokenTest extends ApiBaseTest {


    private InputDataHelper data;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        data = new InputDataHelper();
    }

    @Test(description = "Create token with valid credentials")
    @Story("Create Token")
    @Severity(SeverityLevel.CRITICAL)
    public void createTokenTest() {
        CreateTokenSteps createTokenSteps = new CreateTokenSteps();
        CreateTokenResponseDTO createTokenResponseDTO = createTokenSteps.createToken(username(),password());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(createTokenResponseDTO.getToken(), "Token should not be null");
        softAssert.assertTrue(!createTokenResponseDTO.getToken().isBlank(), "Token should not be blank");

        softAssert.assertAll();
    }

    @Test(description = "Create token with invalid credentials → CURRENT behavior: 200 + error body")
    @Story("Create Token")
    @Severity(SeverityLevel.NORMAL)
    public void createTokenInvalidCredentialsTes() {
        CreateTokenSteps createTokenSteps = new CreateTokenSteps();
        CreateTokenErrorResponseDTO errorResponseDTO =createTokenSteps.createTokenInvalidCredentials(username(), data.invalidPassword());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDTO.getReason(), BAD_CREDENTIALS, "'reason' should be 'Bad credentials'");
        softAssert.assertTrue(errorResponseDTO.getReason() != null && !errorResponseDTO.getReason().isBlank(), "'reason' should not be blank");

        softAssert.assertAll();
    }

    //New comment is added
    @Issue("RB-001")
    @Test(groups = {"BUG", "CONTRACT"},description = "CONTRACT: Invalid credentials must return 401 ")
    @Severity(SeverityLevel.NORMAL)
    public void createTokenUnauthorizedContractTest() {
        CreateTokenSteps createTokenSteps = new CreateTokenSteps();
        CreateTokenErrorResponseDTO errorResponseStatusCode = createTokenSteps.createTokenUnauthorizedContract(username(), data.invalidPassword());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseStatusCode.getReason(), BAD_CREDENTIALS, "Error 'reason' should be 'Bad credentials'");

        softAssert.assertAll();
    }
}