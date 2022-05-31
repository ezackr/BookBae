package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    @JsonProperty("email")
    public String email;

    @JsonProperty("name")
    public String name;

    @JsonProperty("gender")
    public String gender;

    @JsonProperty("favGenre")
    public String favGenre;

    @JsonProperty("birthday")
    public String birthday;

    @JsonProperty("bio")
    public String bio;

    @JsonProperty("zipcode")
    public String zipcode;

}