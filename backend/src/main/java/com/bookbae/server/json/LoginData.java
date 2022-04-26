package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginData {
    private String username;
    private String password; //TODO: PBKDF2 or something?

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }
}