package com.bytechat.dto;

import java.time.Instant;

public class ContactDto {

    private String id;
    private String contactUserId;
    private String contactName;
    private String contactEmail;
    private String contactProfileImage;
    private String contactPhone;
    private String contactBio;
    private String lastMessage;
    private Instant lastMessageAt;
    private Instant addedAt;
    private int unreadCount;
    private boolean deletedAccount;
    private boolean online;

    public ContactDto() {}

    public ContactDto(String id, String contactUserId, String contactName, String contactEmail, String contactProfileImage, String contactPhone, String contactBio, String lastMessage, Instant lastMessageAt, Instant addedAt, int unreadCount, boolean deletedAccount, boolean online) {
        this.id = id;
        this.contactUserId = contactUserId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactProfileImage = contactProfileImage;
        this.contactPhone = contactPhone;
        this.contactBio = contactBio;
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
        this.addedAt = addedAt;
        this.unreadCount = unreadCount;
        this.deletedAccount = deletedAccount;
        this.online = online;
    }

    public static ContactDtoBuilder builder() {
        return new ContactDtoBuilder();
    }

    public static class ContactDtoBuilder {
        private String id;
        private String contactUserId;
        private String contactName;
        private String contactEmail;
        private String contactProfileImage;
        private String contactPhone;
        private String contactBio;
        private String lastMessage;
        private Instant lastMessageAt;
        private Instant addedAt;
        private int unreadCount;
        private boolean deletedAccount;
        private boolean online;

        public ContactDtoBuilder id(String id) { this.id = id; return this; }
        public ContactDtoBuilder contactUserId(String contactUserId) { this.contactUserId = contactUserId; return this; }
        public ContactDtoBuilder contactName(String contactName) { this.contactName = contactName; return this; }
        public ContactDtoBuilder contactEmail(String contactEmail) { this.contactEmail = contactEmail; return this; }
        public ContactDtoBuilder contactProfileImage(String contactProfileImage) { this.contactProfileImage = contactProfileImage; return this; }
        public ContactDtoBuilder contactPhone(String contactPhone) { this.contactPhone = contactPhone; return this; }
        public ContactDtoBuilder contactBio(String contactBio) { this.contactBio = contactBio; return this; }
        public ContactDtoBuilder lastMessage(String lastMessage) { this.lastMessage = lastMessage; return this; }
        public ContactDtoBuilder lastMessageAt(Instant lastMessageAt) { this.lastMessageAt = lastMessageAt; return this; }
        public ContactDtoBuilder addedAt(Instant addedAt) { this.addedAt = addedAt; return this; }
        public ContactDtoBuilder unreadCount(int unreadCount) { this.unreadCount = unreadCount; return this; }
        public ContactDtoBuilder deletedAccount(boolean deletedAccount) { this.deletedAccount = deletedAccount; return this; }
        public ContactDtoBuilder online(boolean online) { this.online = online; return this; }

        public ContactDto build() {
            return new ContactDto(id, contactUserId, contactName, contactEmail, contactProfileImage, contactPhone, contactBio, lastMessage, lastMessageAt, addedAt, unreadCount, deletedAccount, online);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getContactUserId() { return contactUserId; }
    public void setContactUserId(String contactUserId) { this.contactUserId = contactUserId; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getContactProfileImage() { return contactProfileImage; }
    public void setContactProfileImage(String contactProfileImage) { this.contactProfileImage = contactProfileImage; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactBio() { return contactBio; }
    public void setContactBio(String contactBio) { this.contactBio = contactBio; }
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public Instant getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(Instant lastMessageAt) { this.lastMessageAt = lastMessageAt; }
    public Instant getAddedAt() { return addedAt; }
    public void setAddedAt(Instant addedAt) { this.addedAt = addedAt; }
    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
    public boolean isDeletedAccount() { return deletedAccount; }
    public void setDeletedAccount(boolean deletedAccount) { this.deletedAccount = deletedAccount; }
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
}
