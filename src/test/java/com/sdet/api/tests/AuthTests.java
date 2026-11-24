package com.sdet.api.tests;

import com.sdet.api.config.ApiConfig;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.emptyOrNullString;

@Epic("Restful Booker API")
@Feature("Authentication")
public class AuthTests extends BaseApiTest {

    @Test(groups = {"smoke", "auth"}, description = "Valid credentials return a token")
    public void validCredentialsReturnToken() {
        Response response = authClient.login(ApiConfig.authUsername(), ApiConfig.authPassword());

        response.then()
                .statusCode(200)
                .body("token", not(emptyOrNullString()));
    }

    @DataProvider(name = "badCredentials")
    public Object[][] badCredentials() {
        return new Object[][]{
                {"admin", "wrong-password"},
                {"not-a-user", "password123"}
        };
    }

    /**
     * API quirk: Restful-Booker answers 200 with {"reason":"Bad credentials"} instead of 401.
     * The test documents the real behaviour; a real project would raise this as a defect.
     */
    @Test(dataProvider = "badCredentials", groups = {"regression", "auth"},
            description = "Invalid credentials are rejected with a reason and no token")
    public void invalidCredentialsAreRejected(String username, String password) {
        authClient.login(username, password)
                .then()
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"))
                .body("token", nullValue());
    }
}
