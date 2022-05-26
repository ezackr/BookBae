package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import java.sql.Timestamp;

public class ChatLineResponse {
    @JsonProperty("userId")
    public String userId; // sender

    @JsonProperty("timestamp")
    public Timestamp timestamp; // when sent

    @JsonProperty("text")
    public String text;

    @JsonProperty("nthMessage")
    public Integer nthMessage; // this messages' order

}