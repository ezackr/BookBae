package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRequest {
    @JsonProperty("email")
    public String email;

    @JsonProperty("password")
    public String password;

}