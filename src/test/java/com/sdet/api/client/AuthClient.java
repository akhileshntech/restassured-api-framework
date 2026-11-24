package com.sdet.api.client;

import com.sdet.api.config.ApiConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthClient extends BaseClient {

    @Step("POST /auth as {0}")
    public Response login(String username, String password) {
        return given()
                .spec(spec())
                .body(Map.of("username", username, "password", password))
                .when()
                .post("/auth");
    }

    /** Logs in with the configured credentials and returns the token. */
    public String getToken() {
        return login(ApiConfig.authUsername(), ApiConfig.authPassword())
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}
