package com.restfulbooker.helpers;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.specification.FilterableRequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class CustomRequestSpecification {

    private final FilterableRequestSpecification spec;

    public CustomRequestSpecification() {
        spec = (FilterableRequestSpecification) given();
    }


    public void addBaseUri(String baseUri)       { spec.baseUri(baseUri); }
    public void addBasePath(String basePath)     { spec.basePath(basePath); }
    public void setContentType(ContentType type) { spec.contentType(type); }
    public void setRelaxedHttpsValidation()      { spec.relaxedHTTPSValidation(); }
    public FilterableRequestSpecification getFilterableRequestSpecification() { return spec; }


    public void addBodyToRequest(Object body) { spec.body(body); }
    public void clearBody()                   { spec.body((Object) null); }


    public void addCustomHeader(String name, String value) {
        removeCustomHeader(name);
        spec.header(name, value);
    }
    public void removeCustomHeader(String name)  { spec.removeHeader(name); }
    public void addHeaders(Headers headers) {
        if (headers != null) for (Header h : headers) addCustomHeader(h.getName(), h.getValue());
    }


    public void setCookie(String name, String value) {
        try { spec.removeCookie(name); } catch (Throwable ignored) {}
        spec.removeHeader("Cookie");

        spec.cookie(name, value == null ? "" : value);
    }

    public void setTokenCookie(String token) {
        setCookie("token", token);
    }

    public void clearCookies() {
        try { spec.removeCookie("token"); } catch (Throwable ignored) {}
        spec.removeHeader("Cookie");
    }

    public void addPathParams(Map<String, ?> params) {
        for (String k : spec.getNamedPathParams().keySet()) spec.removeNamedPathParam(k);
        spec.pathParams(params);
    }
    public void addPathParam(String name, Object value) {
        spec.removeNamedPathParam(name);
        spec.pathParam(name, value);
    }
    public void addQueryParams(Map<String, ?> params) {
        for (String k : spec.getQueryParams().keySet()) spec.removeQueryParam(k);
        spec.queryParams(params);
    }
}