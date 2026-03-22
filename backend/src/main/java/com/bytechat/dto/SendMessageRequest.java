package com.bytechat.dto;

import com.bytechat.model.Message.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SendMessageRequest {

    @NotBlank(message = "Receiver ID is required")
    private String receiverId;

    private String message;

    @NotNull(message = "Message type is required")
    private MessageType messageType;

    private String fileUrl;

    public SendMessageRequest() {}

    public SendMessageRequest(String receiverId, String message, MessageType messageType, String fileUrl) {
        this.receiverId = receiverId;
        this.message = message;
        this.messageType = messageType;
        this.fileUrl = fileUrl;
    }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public MessageType getMessageType() { return messageType; }
    public void setMessageType(MessageType messageType) { this.messageType = messageType; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
}
