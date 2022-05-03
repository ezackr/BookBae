package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    protected UUID userid;
    protected String name;
    protected String preferredGender;
    protected String gender;
    protected String favGenre;
    protected String birthday;
    protected String bio;
    protected String phoneNumber;
    protected String email;
    protected String zipcode;

    public UserResponse() {}

    public UserResponse(UserRequest req) {
        this.userid = req.userid;
        this.name = req.name;
        this.preferredGender = req.preferredGender;
        this.gender = req.gender;
        this.favGenre = req.favGenre;
        this.birthday = req.birthday;
        this.bio = req.bio;
        this.phoneNumber = req.phoneNumber;
        this.email = req.email;
        this.zipcode = req.zipcode;
    }

    @JsonProperty("username")
    public String getUserId() {
        return userid.toString();
    }

    @JsonProperty("username")
    public void setUserId(UUID userid) {
        this.userid = userid;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("preferredGender")
    public String getPreferredGender() {
        return preferredGender;
    }

    @JsonProperty("preferredGender")
    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("favGenre")
    public String getFavGenre() { return favGenre; }

    @JsonProperty("favGenre")
    public void setFavGenre(String favGenre) {
        this.favGenre = favGenre;
    }

    @JsonProperty("birthday")
    public String getBirthday() {
        return birthday;
    }

    @JsonProperty("birthday")
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @JsonProperty("bio")
    public String getBio() {
        return bio;
    }

    @JsonProperty("bio")
    public void setBio(String bio) {
        this.bio = bio;
    }

    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("zipcode")
    public String getZipcode() {
        return zipcode;
    }

    @JsonProperty("zipcode")
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}