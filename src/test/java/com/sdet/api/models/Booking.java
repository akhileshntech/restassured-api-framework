package com.sdet.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Request and response body for a booking. Field names match the API's JSON exactly. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Booking(
        String firstname,
        String lastname,
        int totalprice,
        boolean depositpaid,
        BookingDates bookingdates,
        String additionalneeds) {
}
