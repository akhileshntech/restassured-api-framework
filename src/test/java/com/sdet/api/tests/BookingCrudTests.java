package com.sdet.api.tests;

import com.sdet.api.models.Booking;
import com.sdet.api.models.BookingResponse;
import com.sdet.api.utils.BookingDataFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;

@Epic("Restful Booker API")
@Feature("Booking CRUD")
public class BookingCrudTests extends BaseApiTest {

    /** Each test creates its own booking, so tests are independent and can run in any order. */
    private BookingResponse createBooking(Booking booking) {
        Response response = bookingClient.createBooking(booking);
        response.then().statusCode(200);
        return response.as(BookingResponse.class);
    }

    @Test(groups = {"smoke", "crud"}, description = "POST /booking creates a booking that matches the contract")
    public void createBookingReturnsCreatedBooking() {
        Booking request = BookingDataFactory.randomBooking();

        Response response = bookingClient.createBooking(request);

        response.then()
                .statusCode(200)
                .time(lessThan(10000L))
                .body(matchesJsonSchemaInClasspath("schemas/create-booking-response-schema.json"));

        BookingResponse body = response.as(BookingResponse.class);
        Assert.assertTrue(body.bookingid() > 0, "A booking id should be returned");
        Assert.assertEquals(body.booking(), request, "Stored booking should equal the request");
    }

    @Test(groups = {"smoke", "crud"}, description = "GET /booking/{id} returns the booking that was created")
    public void getBookingReturnsCreatedBooking() {
        Booking request = BookingDataFactory.randomBooking();
        int id = createBooking(request).bookingid();

        Response response = bookingClient.getBooking(id);

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
        Assert.assertEquals(response.as(Booking.class), request);
    }

    @Test(groups = {"regression", "crud"}, description = "GET /booking lists existing booking ids")
    public void getBookingIdsReturnsNonEmptyList() {
        Response response = bookingClient.getBookingIds();

        response.then().statusCode(200);
        List<Integer> ids = response.jsonPath().getList("bookingid");
        Assert.assertFalse(ids.isEmpty(), "Expected at least one booking id");
    }

    @Test(groups = {"regression", "crud"}, description = "PUT /booking/{id} replaces the whole booking")
    public void updateBookingReplacesAllFields() {
        int id = createBooking(BookingDataFactory.randomBooking()).bookingid();
        Booking updated = BookingDataFactory.randomBooking();

        Response response = bookingClient.updateBooking(id, updated, token);

        response.then().statusCode(200);
        Assert.assertEquals(response.as(Booking.class), updated);
        // confirm the change was persisted, not just echoed back
        Assert.assertEquals(bookingClient.getBooking(id).as(Booking.class), updated);
    }

    @Test(groups = {"regression", "crud"}, description = "PATCH /booking/{id} changes only the supplied fields")
    public void partialUpdateChangesOnlySuppliedFields() {
        Booking original = BookingDataFactory.randomBooking();
        int id = createBooking(original).bookingid();

        Response response = bookingClient.partialUpdateBooking(
                id, Map.of("firstname", "James", "lastname", "Brown"), token);

        response.then().statusCode(200);
        Booking patched = response.as(Booking.class);
        Assert.assertEquals(patched.firstname(), "James");
        Assert.assertEquals(patched.lastname(), "Brown");
        Assert.assertEquals(patched.totalprice(), original.totalprice(), "Untouched field must not change");
        Assert.assertEquals(patched.bookingdates(), original.bookingdates(), "Untouched field must not change");
    }

    /** API quirk: DELETE answers 201 Created rather than 200/204. */
    @Test(groups = {"regression", "crud"}, description = "DELETE /booking/{id} removes the booking")
    public void deleteBookingRemovesIt() {
        int id = createBooking(BookingDataFactory.randomBooking()).bookingid();

        bookingClient.deleteBooking(id, token).then().statusCode(201);

        bookingClient.getBooking(id).then().statusCode(404);
    }
}
