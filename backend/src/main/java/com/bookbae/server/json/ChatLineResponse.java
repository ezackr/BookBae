package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import java.sql.Timestamp;

public class ChatLineResponse {
    private String userId; // sender
    private Timestamp timestamp; // when sent
    private String text;
    private Integer nthMessage; // this messages' order

    @JsonProperty("userId")
    public String getUserId() {
        return this.userId;// == null ? null : this.likeId.toString();
    }

    @JsonProperty("userId")
    public void setUserId(String userId) { this.userId = userId; }

    @JsonProperty("timestamp")
    public Timestamp getTimestamp() { return this.timestamp; }

    @JsonProperty("timestamp")
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    @JsonProperty("text")
    public String getText() { return this.text; }

    @JsonProperty("text")
    public void setText(String text) { this.text = text; }

    @JsonProperty("nthMessage")
    public Integer getNthMessage() { return this.nthMessage; }

    @JsonProperty("nthMessage")
    public void setNthMessage(Integer nthMessage) { this.nthMessage = nthMessage; }


}