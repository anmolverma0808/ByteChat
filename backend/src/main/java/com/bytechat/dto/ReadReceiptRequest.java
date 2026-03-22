package com.bytechat.dto;

import jakarta.validation.constraints.NotBlank;

public class ReadReceiptRequest {

    @NotBlank(message = "Sender ID is required")
    private String senderId;

    private String readerId; // The ID of the person who read the messages

    public ReadReceiptRequest() {}

    public ReadReceiptRequest(String senderId) {
        this.senderId = senderId;
    }

    public ReadReceiptRequest(String senderId, String readerId) {
        this.senderId = senderId;
        this.readerId = readerId;
    }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getReaderId() { return readerId; }
    public void setReaderId(String readerId) { this.readerId = readerId; }
}
