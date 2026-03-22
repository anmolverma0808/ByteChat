package com.bytechat.dto;

public class AuthResponse {

    private String token;
    private String userId;
    private String email;
    private String fullName;

    private String phone;
    private String profileImage;
    private String avatarName;
    private String bio;

    public AuthResponse() {}

    public AuthResponse(String token, String userId, String email, String fullName, String phone, String profileImage, String avatarName, String bio) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.profileImage = profileImage;
        this.avatarName = avatarName;
        this.bio = bio;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private String token;
        private String userId;
        private String email;
        private String fullName;
        private String phone;
        private String profileImage;
        private String avatarName;
        private String bio;

        public AuthResponseBuilder token(String token) { this.token = token; return this; }
        public AuthResponseBuilder userId(String userId) { this.userId = userId; return this; }
        public AuthResponseBuilder email(String email) { this.email = email; return this; }
        public AuthResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public AuthResponseBuilder phone(String phone) { this.phone = phone; return this; }
        public AuthResponseBuilder profileImage(String profileImage) { this.profileImage = profileImage; return this; }
        public AuthResponseBuilder avatarName(String avatarName) { this.avatarName = avatarName; return this; }
        public AuthResponseBuilder bio(String bio) { this.bio = bio; return this; }

        public AuthResponse build() {
            return new AuthResponse(token, userId, email, fullName, phone, profileImage, avatarName, bio);
        }
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    public String getAvatarName() { return avatarName; }
    public void setAvatarName(String avatarName) { this.avatarName = avatarName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
