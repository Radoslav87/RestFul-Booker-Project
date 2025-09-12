package com.restfulbooker.base;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.JsonConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.config.JsonPathConfig;
import org.testng.annotations.BeforeSuite;

public class ApiBaseTest extends BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void initSuiteApi() {
        super.initSuiteBase();

        // 1) Base URL (fail fast)
        String base = cfg("baseUri");
        if (base == null || base.isBlank()) {
            throw new IllegalStateException("Missing 'baseUri' in config or -DbaseUri");
        }
        RestAssured.baseURI = base;

        // 2) Allure filter
        var allure = new AllureRestAssured();

        // 3) Строг Jackson ObjectMapper:
        // - JavaTimeModule за LocalDate
        // - ISO дати (yyyy-MM-dd), не timestamps
        // - fail при непознати полета/коерсии/плаващи към int и т.н.
        ObjectMapper strictMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false)
                .configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, false);

        // 4) Глобална RestAssured конфигурация
        RestAssured.config = RestAssured.config()
                .encoderConfig(EncoderConfig.encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false))
                .logConfig(LogConfig.logConfig()
                        .blacklistHeader("Authorization", "Cookie", "token"))
                .objectMapperConfig(
                        ObjectMapperConfig.objectMapperConfig()
                                .jackson2ObjectMapperFactory((cls, charset) -> strictMapper)
                )
                // по-точни числа в JsonPath (без double артефакти)
                .jsonConfig(JsonConfig.jsonConfig()
                        .numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));

        // 5) Логове
        boolean verbose = Boolean.parseBoolean(cfg("isLogEnabled", "false"));
        if (verbose) {
            RestAssured.replaceFiltersWith(
                    allure,
                    new RequestLoggingFilter(LogDetail.ALL),
                    new ResponseLoggingFilter(LogDetail.ALL)
            );
        } else {
            RestAssured.replaceFiltersWith(allure);
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }
    }

    protected String username() {
        return cfg("username");
    }
    protected String password() {
        return cfg("password");
    }
}