package com.bytechat.dto;

public class SearchResultDto {
    private MessageDto message;
    private String contactName;
    private String contactId;
    private String contactProfileImage;

    public SearchResultDto() {}

    public SearchResultDto(MessageDto message, String contactName, String contactId, String contactProfileImage) {
        this.message = message;
        this.contactName = contactName;
        this.contactId = contactId;
        this.contactProfileImage = contactProfileImage;
    }

    public static SearchResultDtoBuilder builder() {
        return new SearchResultDtoBuilder();
    }

    public static class SearchResultDtoBuilder {
        private MessageDto message;
        private String contactName;
        private String contactId;
        private String contactProfileImage;

        public SearchResultDtoBuilder message(MessageDto message) { this.message = message; return this; }
        public SearchResultDtoBuilder contactName(String contactName) { this.contactName = contactName; return this; }
        public SearchResultDtoBuilder contactId(String contactId) { this.contactId = contactId; return this; }
        public SearchResultDtoBuilder contactProfileImage(String contactProfileImage) { this.contactProfileImage = contactProfileImage; return this; }

        public SearchResultDto build() {
            return new SearchResultDto(message, contactName, contactId, contactProfileImage);
        }
    }

    public MessageDto getMessage() { return message; }
    public void setMessage(MessageDto message) { this.message = message; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactId() { return contactId; }
    public void setContactId(String contactId) { this.contactId = contactId; }
    public String getContactProfileImage() { return contactProfileImage; }
    public void setContactProfileImage(String contactProfileImage) { this.contactProfileImage = contactProfileImage; }
}
