package com.bytechat.dto;

import com.bytechat.model.Message.MessageType;
import java.time.Instant;

public class MessageDto {

    private String id;
    private String senderId;
    private String receiverId;
    private String message;
    private MessageType messageType;
    private String fileUrl;
    private Instant createdAt;
    private boolean readStatus;

    public MessageDto() {}

    public MessageDto(String id, String senderId, String receiverId, String message, MessageType messageType, String fileUrl, Instant createdAt, boolean readStatus) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.messageType = messageType;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt;
        this.readStatus = readStatus;
    }

    public static MessageDtoBuilder builder() {
        return new MessageDtoBuilder();
    }

    public static class MessageDtoBuilder {
        private String id;
        private String senderId;
        private String receiverId;
        private String message;
        private MessageType messageType;
        private String fileUrl;
        private Instant createdAt;
        private boolean readStatus;

        public MessageDtoBuilder id(String id) { this.id = id; return this; }
        public MessageDtoBuilder senderId(String senderId) { this.senderId = senderId; return this; }
        public MessageDtoBuilder receiverId(String receiverId) { this.receiverId = receiverId; return this; }
        public MessageDtoBuilder message(String message) { this.message = message; return this; }
        public MessageDtoBuilder messageType(MessageType messageType) { this.messageType = messageType; return this; }
        public MessageDtoBuilder fileUrl(String fileUrl) { this.fileUrl = fileUrl; return this; }
        public MessageDtoBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public MessageDtoBuilder readStatus(boolean readStatus) { this.readStatus = readStatus; return this; }

        public MessageDto build() {
            return new MessageDto(id, senderId, receiverId, message, messageType, fileUrl, createdAt, readStatus);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public MessageType getMessageType() { return messageType; }
    public void setMessageType(MessageType messageType) { this.messageType = messageType; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public boolean isReadStatus() { return readStatus; }
    public void setReadStatus(boolean readStatus) { this.readStatus = readStatus; }
}
