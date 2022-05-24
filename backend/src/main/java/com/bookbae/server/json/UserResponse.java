package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
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

    @JsonProperty("userid")
    public String userId;

    public UserResponse() {}

    public UserResponse(UserRequest req) {
        this.email = req.email;
        this.name = req.name;
        this.gender = req.gender;
        this.favGenre = req.favGenre;
        this.birthday = req.birthday;
        this.bio = req.bio;
        this.zipcode = req.zipcode;
    }

}