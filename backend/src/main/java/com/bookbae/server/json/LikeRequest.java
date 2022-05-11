package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LikeRequest {
    //represents person being liked
    private String userid;

    @JsonProperty("userid")
    public String getUserId() {
        return userid;
    }

    @JsonProperty("userid")
    public void setUserId(String userid) {
        this.userid = userid;
    }
}