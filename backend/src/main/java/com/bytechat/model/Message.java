package com.bytechat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "messages")
public class Message {

    @Id
    private String id;
    @Indexed
    private String senderId;
    @Indexed
    private String receiverId;
    private String message;
    private MessageType messageType = MessageType.TEXT;
    private String fileUrl;
    private Instant createdAt = Instant.now();
    private boolean readStatus = false;

    public Message() {}

    public Message(String id, String senderId, String receiverId, String message, MessageType messageType, String fileUrl, Instant createdAt, boolean readStatus) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.messageType = messageType != null ? messageType : MessageType.TEXT;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.readStatus = readStatus;
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public static class MessageBuilder {
        private String id;
        private String senderId;
        private String receiverId;
        private String message;
        private MessageType messageType;
        private String fileUrl;
        private Instant createdAt;
        private boolean readStatus;

        public MessageBuilder id(String id) { this.id = id; return this; }
        public MessageBuilder senderId(String senderId) { this.senderId = senderId; return this; }
        public MessageBuilder receiverId(String receiverId) { this.receiverId = receiverId; return this; }
        public MessageBuilder message(String message) { this.message = message; return this; }
        public MessageBuilder messageType(MessageType messageType) { this.messageType = messageType; return this; }
        public MessageBuilder fileUrl(String fileUrl) { this.fileUrl = fileUrl; return this; }
        public MessageBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public MessageBuilder readStatus(boolean readStatus) { this.readStatus = readStatus; return this; }

        public Message build() {
            return new Message(id, senderId, receiverId, message, messageType, fileUrl, createdAt, readStatus);
        }
    }

    public enum MessageType {
        TEXT, FILE
    }

    // Getters and Setters
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
