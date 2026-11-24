package com.sdet.api.client;

import com.sdet.api.models.Booking;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * One method per endpoint. Methods return the raw Response so each test decides what to assert,
 * including negative cases that the API client should never hide.
 */
public class BookingClient extends BaseClient {

    private static final String BOOKING = "/booking";
    private static final String BOOKING_BY_ID = "/booking/{id}";

    @Step("POST /booking")
    public Response createBooking(Booking booking) {
        return given().spec(spec()).body(booking).when().post(BOOKING);
    }

    @Step("GET /booking")
    public Response getBookingIds() {
        return given().spec(spec()).when().get(BOOKING);
    }

    @Step("GET /booking/{0}")
    public Response getBooking(int id) {
        return given().spec(spec()).when().get(BOOKING_BY_ID, id);
    }

    /** Pass a null token to call the endpoint without credentials. */
    @Step("PUT /booking/{0}")
    public Response updateBooking(int id, Booking booking, String token) {
        return withAuth(token).body(booking).when().put(BOOKING_BY_ID, id);
    }

    @Step("PATCH /booking/{0}")
    public Response partialUpdateBooking(int id, Map<String, Object> fields, String token) {
        return withAuth(token).body(fields).when().patch(BOOKING_BY_ID, id);
    }

    @Step("DELETE /booking/{0}")
    public Response deleteBooking(int id, String token) {
        return withAuth(token).when().delete(BOOKING_BY_ID, id);
    }

    private RequestSpecification withAuth(String token) {
        RequestSpecification request = given().spec(spec());
        if (token != null) {
            request.header("Cookie", "token=" + token);
        }
        return request;
    }
}
