package com.sdet.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingDates(String checkin, String checkout) {
}
