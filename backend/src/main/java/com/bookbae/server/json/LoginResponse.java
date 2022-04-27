package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    private String jws;
    public LoginResponse(String jws) {
        this.jws = jws;
    }

    @JsonProperty("authToken")
    public String getAuthToken() {
        return this.jws;
    }
}