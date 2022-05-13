package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PreferencesMessage {
    @JsonProperty
    public int lowerAgeLimit;

    @JsonProperty
    public int upperAgeLimit;

    @JsonProperty
    public int withinXMiles;
}