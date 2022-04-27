package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class AccountCreationResponse {
    private UUID userid;

    public AccountCreationResponse(UUID userid) {
        this.userid = userid;
    }

    @JsonProperty("username")
    public String getUsername() {
        return userid.toString();
    }
}