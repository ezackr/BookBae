package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class LoginRequest {
    private UUID userid;
    private String password; //TODO: PBKDF2 or something?
    private boolean isValid = true;

    @JsonProperty("username")
    public String getUsername() {
        return userid.toString();
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        try {
            userid = UUID.fromString(username);
        } catch (IllegalArgumentException e) {
            isValid = false;
        }
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        return this.isValid;
    }
}