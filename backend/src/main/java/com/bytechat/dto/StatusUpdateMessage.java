package com.bytechat.dto;

public class StatusUpdateMessage {
    private String userId;
    private boolean online;

    public StatusUpdateMessage() {}

    public StatusUpdateMessage(String userId, boolean online) {
        this.userId = userId;
        this.online = online;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
}
