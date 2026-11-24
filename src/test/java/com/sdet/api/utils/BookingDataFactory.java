package com.sdet.api.utils;

import com.sdet.api.models.Booking;
import com.sdet.api.models.BookingDates;
import net.datafaker.Faker;

import java.time.LocalDate;

/** Generates unique, realistic test data so tests never depend on hard-coded records. */
public final class BookingDataFactory {

    private static final Faker FAKER = new Faker();

    private BookingDataFactory() {
    }

    public static Booking randomBooking() {
        LocalDate checkin = LocalDate.now().plusDays(FAKER.number().numberBetween(1, 30));
        LocalDate checkout = checkin.plusDays(FAKER.number().numberBetween(1, 10));

        return new Booking(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.number().numberBetween(100, 1000),
                FAKER.bool().bool(),
                new BookingDates(checkin.toString(), checkout.toString()),
                FAKER.options().option("Breakfast", "Dinner", "Late checkout", "Airport pickup"));
    }
}
