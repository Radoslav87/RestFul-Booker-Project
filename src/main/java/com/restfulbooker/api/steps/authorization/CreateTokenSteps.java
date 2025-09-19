package com.restfulbooker.api.steps.authorization;

import com.restfulbooker.api.dtos.authorization.CreateTokenErrorResponseDTO;
import com.restfulbooker.api.dtos.authorization.CreateTokenRequestDTO;
import com.restfulbooker.api.dtos.authorization.CreateTokenResponseDTO;
import com.restfulbooker.helpers.CustomRequestSpecification;
import com.restfulbooker.helpers.RequestOperationsHelper;
import com.restfulbooker.api.factories.authorization.CreateTokenFactory;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.restfulbooker.constants.BasePathsConstants.POST_AUTHENTICATION;

public class CreateTokenSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public CreateTokenSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_AUTHENTICATION);
        requestSpecification.setContentType(ContentType.JSON);
    }

    @Step("Create token successfully")
    public CreateTokenResponseDTO createToken(String username, String password) {

        return createTokenResponse(username, password, HttpStatus.SC_OK)

                .as(CreateTokenResponseDTO.class);

    }

    @Step("Create token with invalid credentials")
    public CreateTokenErrorResponseDTO createTokenInvalidCredentials(String username, String password) {

        return createTokenResponse(username, password, HttpStatus.SC_OK)

                .as(CreateTokenErrorResponseDTO.class);
    }

    @Step("Create token successfully")
    public CreateTokenErrorResponseDTO createTokenUnauthorizedContract(String username, String badPassword) {

        return createTokenResponse(username, badPassword, HttpStatus.SC_UNAUTHORIZED)

                .as(CreateTokenErrorResponseDTO.class);
    }

    private Response createTokenResponse(String username, String password, int expectedStatusCode) {
        CreateTokenFactory createTokenFactory = new CreateTokenFactory();
        CreateTokenRequestDTO createTokenFactoryRequestDto = createTokenFactory.
                createTokenRequest(username, password);

        requestSpecification.addBodyToRequest(createTokenFactoryRequestDto);
        Response response = requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(expectedStatusCode);
        return response;

    }
}