package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatRequest {

    private String text;

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }
}