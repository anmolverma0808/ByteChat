package com.bytechat.dto;

import jakarta.validation.constraints.NotBlank;

public class TypingRequest {

    private String senderId;
    private String receiverId;
    private boolean isTyping;

    public TypingRequest() {}

    public TypingRequest(String senderId, String receiverId, boolean isTyping) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.isTyping = isTyping;
    }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public boolean getIsTyping() { return isTyping; }
    public void setIsTyping(boolean isTyping) { this.isTyping = isTyping; }
}
