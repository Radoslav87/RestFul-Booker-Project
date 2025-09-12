package com.restfulbooker.api.helper;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RequestOperationsHelper {

    private Response send(RequestSpecification req, Method method) {
        return given()
                 .filter(new AllureRestAssured())
                .spec(req)
                .when()
                .request(method);
    }

    public Response sendGetRequest(RequestSpecification req) {
        return send(req, Method.GET);
    }

    public Response sendPostRequest(RequestSpecification req) {
        return send(req, Method.POST);
    }

    public Response sendPutRequest(RequestSpecification req) {
        return send(req, Method.PUT);
    }

    public Response sendPatchRequest(RequestSpecification req) {
        return send(req, Method.PATCH);
    }

    public Response sendDeleteRequest(RequestSpecification req) {
        return send(req, Method.DELETE);
    }

}
