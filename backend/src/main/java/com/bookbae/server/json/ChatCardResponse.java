package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class ChatCardResponse {
    private String displayName;
    private String photoUrl;
    private String lastMessage;
    private UUID likeId;

    @JsonProperty("displayName")
    public String getDisplayName() {
        return this.displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("photoUrl")
    public String getPhotoUrl() {
        return this.photoUrl;
    }

    @JsonProperty("lastMessage")
    public String getLastMessage() {
        return this.lastMessage;
    }

    @JsonProperty("likeId")
    public String getLikeId() {
        return likeId == null ? null : this.likeId.toString();
    }

    @JsonProperty("photoUrl")
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @JsonProperty("lastMessage")
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLikeId(UUID likeId) {
        this.likeId = likeId;
    }
}