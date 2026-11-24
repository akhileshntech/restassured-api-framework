package com.sdet.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Response of POST /booking: the new id plus the stored booking. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingResponse(int bookingid, Booking booking) {
}
