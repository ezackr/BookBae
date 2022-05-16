package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailResponse {
    private boolean doesEmailExist;

    @JsonProperty("emailExists")
    public boolean getDoesEmailExist() {
        return doesEmailExist;
    }

    @JsonProperty("emailExists")
    public void setDoesEmailExist(boolean doesEmailExist) {
        this.doesEmailExist = doesEmailExist;
    }
}