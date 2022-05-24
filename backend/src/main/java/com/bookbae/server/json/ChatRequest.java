package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatRequest {
    @JsonProperty("text")
    public String text;

}