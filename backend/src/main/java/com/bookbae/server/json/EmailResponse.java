package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailResponse {
    @JsonProperty("emailExists")
    public boolean doesEmailExist;

}