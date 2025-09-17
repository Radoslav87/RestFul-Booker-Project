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
        requestSpecification    = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_AUTHENTICATION);
        requestSpecification.setContentType(ContentType.JSON);
    }

    @Step("Create token (200 OK)")
    public CreateTokenResponseDTO createToken(String username, String password){
        Response response = createTokenResponse(username,password);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(CreateTokenResponseDTO.class);

    }

    @Step("Create token with invalid credentials (CURRENT behavior: 200 + error body)")
    public CreateTokenErrorResponseDTO createTokenInvalidCredentials(String username, String password) {
        Response response = createTokenResponse(username, password);

        response.then().statusCode(HttpStatus.SC_OK);
        return response.as(CreateTokenErrorResponseDTO.class);
    }

    @Step("Create token with invalid credentials MUST be 401 (CONTRACT)")
    public CreateTokenErrorResponseDTO createTokenUnauthorizedContract(String username, String badPassword) {
        Response response = createTokenResponse(username, badPassword);

        response.then().statusCode(HttpStatus.SC_UNAUTHORIZED);
        return response.as(CreateTokenErrorResponseDTO.class);
    }

    private Response createTokenResponse(String username, String password){
        CreateTokenFactory createTokenFactory = new CreateTokenFactory();
        CreateTokenRequestDTO createTokenFactoryRequestDto = createTokenFactory.
                createTokenRequest(username,password);

        requestSpecification.addBodyToRequest(createTokenFactoryRequestDto);
        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());

    }
}