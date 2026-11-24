package com.sdet.api.tests;

import com.sdet.api.models.Booking;
import com.sdet.api.models.BookingResponse;
import com.sdet.api.utils.BookingDataFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Epic("Restful Booker API")
@Feature("Negative scenarios")
public class BookingNegativeTests extends BaseApiTest {

    private int createBookingId() {
        return bookingClient.createBooking(BookingDataFactory.randomBooking())
                .as(BookingResponse.class)
                .bookingid();
    }

    @Test(groups = {"regression", "negative"}, description = "GET of a non-existent booking returns 404")
    public void getUnknownBookingReturnsNotFound() {
        bookingClient.getBooking(999_999_999).then().statusCode(404);
    }

    @Test(groups = {"regression", "negative"}, description = "PUT without a token is forbidden")
    public void updateWithoutTokenIsForbidden() {
        int id = createBookingId();
        Booking update = BookingDataFactory.randomBooking();

        bookingClient.updateBooking(id, update, null).then().statusCode(403);
    }

    @Test(groups = {"regression", "negative"}, description = "DELETE with an invalid token is forbidden")
    public void deleteWithInvalidTokenIsForbidden() {
        int id = createBookingId();

        bookingClient.deleteBooking(id, "invalid-token").then().statusCode(403);
    }
}
