package com.sdet.api.client;

import com.sdet.api.config.ApiConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Shared request setup: base URI, JSON headers, Allure request/response attachments,
 * and console logging only when an assertion fails (keeps CI logs readable).
 */
public abstract class BaseClient {

    protected RequestSpecification spec() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiConfig.baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept("application/json")
                .addFilter(new AllureRestAssured())
                .setConfig(RestAssuredConfig.config().logConfig(
                        LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
                .build();
    }
}
