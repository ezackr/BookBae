package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class ChatCardResponse {
    @JsonProperty("displayName")
    public String displayName;

    @JsonProperty("photoUrl")
    public String photoUrl;

    @JsonProperty("likeId")
    public String likeId;

}