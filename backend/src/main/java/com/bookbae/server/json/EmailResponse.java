package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailResponse {
    private boolean isEmailValid;

    @JsonProperty("emailExists")
    public boolean getIsEmailValid() {
        return isEmailValid;
    }

    @JsonProperty("emailExists")
    public void setIsEmailValid(boolean isEmailValid) {
        this.isEmailValid = isEmailValid;
    }
}