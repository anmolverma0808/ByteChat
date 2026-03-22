package com.bytechat.dto;

public class UserProfileDto {

    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String timezone;
    private String profileImage;
    private String avatarName;
    private String bio;
    private boolean notificationsEnabled;
    private boolean online;

    public UserProfileDto() {}

    public UserProfileDto(String id, String fullName, String email, String phone, String timezone, String profileImage, String avatarName, String bio, boolean notificationsEnabled, boolean online) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.timezone = timezone;
        this.profileImage = profileImage;
        this.avatarName = avatarName;
        this.bio = bio;
        this.notificationsEnabled = notificationsEnabled;
        this.online = online;
    }

    public static UserProfileDtoBuilder builder() {
        return new UserProfileDtoBuilder();
    }

    public static class UserProfileDtoBuilder {
        private String id;
        private String fullName;
        private String email;
        private String phone;
        private String timezone;
        private String profileImage;
        private String avatarName;
        private String bio;
        private boolean notificationsEnabled;
        private boolean online;

        public UserProfileDtoBuilder id(String id) { this.id = id; return this; }
        public UserProfileDtoBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserProfileDtoBuilder email(String email) { this.email = email; return this; }
        public UserProfileDtoBuilder phone(String phone) { this.phone = phone; return this; }
        public UserProfileDtoBuilder timezone(String timezone) { this.timezone = timezone; return this; }
        public UserProfileDtoBuilder profileImage(String profileImage) { this.profileImage = profileImage; return this; }
        public UserProfileDtoBuilder avatarName(String avatarName) { this.avatarName = avatarName; return this; }
        public UserProfileDtoBuilder bio(String bio) { this.bio = bio; return this; }
        public UserProfileDtoBuilder notificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; return this; }
        public UserProfileDtoBuilder online(boolean online) { this.online = online; return this; }

        public UserProfileDto build() {
            return new UserProfileDto(id, fullName, email, phone, timezone, profileImage, avatarName, bio, notificationsEnabled, online);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    public String getAvatarName() { return avatarName; }
    public void setAvatarName(String avatarName) { this.avatarName = avatarName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
}
