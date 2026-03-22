package com.bytechat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String fullName;
    @Indexed(unique = true)
    private String email;
    private String phone;
    private String password;
    private String timezone;
    private String profileImage;
    private String avatarName;
    private String bio;
    private boolean notificationsEnabled = true;
    private boolean deleted = false;
    private boolean online = false;
    private Instant createdAt = Instant.now();

    public User() {}

    public User(String id, String fullName, String email, String phone, String password, String timezone, String profileImage, String avatarName, String bio, boolean notificationsEnabled, boolean deleted, boolean online, Instant createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.timezone = timezone;
        this.profileImage = profileImage;
        this.avatarName = avatarName;
        this.bio = bio;
        this.notificationsEnabled = notificationsEnabled;
        this.deleted = deleted;
        this.online = online;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    // Builder pattern equivalent
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private String id;
        private String fullName;
        private String email;
        private String phone;
        private String password;
        private String timezone;
        private String profileImage;
        private String avatarName;
        private String bio;
        private boolean notificationsEnabled = true;
        private boolean deleted = false;
        private boolean online = false;
        private Instant createdAt;

        public UserBuilder id(String id) { this.id = id; return this; }
        public UserBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder phone(String phone) { this.phone = phone; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder timezone(String timezone) { this.timezone = timezone; return this; }
        public UserBuilder profileImage(String profileImage) { this.profileImage = profileImage; return this; }
        public UserBuilder avatarName(String avatarName) { this.avatarName = avatarName; return this; }
        public UserBuilder bio(String bio) { this.bio = bio; return this; }
        public UserBuilder notificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; return this; }
        public UserBuilder deleted(boolean deleted) { this.deleted = deleted; return this; }
        public UserBuilder online(boolean online) { this.online = online; return this; }
        public UserBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public User build() {
            return new User(id, fullName, email, phone, password, timezone, profileImage, avatarName, bio, notificationsEnabled, deleted, online, createdAt);
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    public String getAvatarName() { return avatarName; }
    public void setAvatarName(String avatarName) { this.avatarName = avatarName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
}
