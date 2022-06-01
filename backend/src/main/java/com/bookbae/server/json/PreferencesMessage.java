package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PreferencesMessage {
    @JsonProperty
    public Integer lowerAgeLimit;

    @JsonProperty
    public Integer upperAgeLimit;

    @JsonProperty
    public Integer withinXMiles;

    @JsonProperty
    public String preferredGender;
}