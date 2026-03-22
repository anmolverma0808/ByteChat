package com.bytechat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "files")
public class FileDocument {

    @Id
    private String id;
    @Indexed
    private String senderId;
    @Indexed
    private String receiverId;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private Instant uploadedAt = Instant.now();
    private boolean isStarred = false;

    public FileDocument() {}

    public FileDocument(String id, String senderId, String receiverId, String fileName, String fileUrl, Long fileSize, Instant uploadedAt, boolean isStarred) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt != null ? uploadedAt : Instant.now();
        this.isStarred = isStarred;
    }

    public static FileDocumentBuilder builder() {
        return new FileDocumentBuilder();
    }

    public static class FileDocumentBuilder {
        private String id;
        private String senderId;
        private String receiverId;
        private String fileName;
        private String fileUrl;
        private Long fileSize;
        private Instant uploadedAt;
        private boolean isStarred;

        public FileDocumentBuilder id(String id) { this.id = id; return this; }
        public FileDocumentBuilder senderId(String senderId) { this.senderId = senderId; return this; }
        public FileDocumentBuilder receiverId(String receiverId) { this.receiverId = receiverId; return this; }
        public FileDocumentBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public FileDocumentBuilder fileUrl(String fileUrl) { this.fileUrl = fileUrl; return this; }
        public FileDocumentBuilder fileSize(Long fileSize) { this.fileSize = fileSize; return this; }
        public FileDocumentBuilder uploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; return this; }
        public FileDocumentBuilder isStarred(boolean isStarred) { this.isStarred = isStarred; return this; }

        public FileDocument build() {
            return new FileDocument(id, senderId, receiverId, fileName, fileUrl, fileSize, uploadedAt, isStarred);
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }
    public boolean isStarred() { return isStarred; }
    public void setStarred(boolean starred) { isStarred = starred; }
}
