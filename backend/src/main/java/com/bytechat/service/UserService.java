package com.bytechat.service;

import com.bytechat.dto.UserProfileDto;
import com.bytechat.model.User;
import com.bytechat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileDto getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToDto(user);
    }

    public UserProfileDto updateUserProfile(String userId, UserProfileDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getTimezone() != null) user.setTimezone(request.getTimezone());
        if (request.getProfileImage() != null) user.setProfileImage(request.getProfileImage());
        if (request.getAvatarName() != null) user.setAvatarName(request.getAvatarName());
        if (request.getBio() != null) user.setBio(request.getBio());
        user.setNotificationsEnabled(request.isNotificationsEnabled());

        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    public List<UserProfileDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }

    private UserProfileDto mapToDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .timezone(user.getTimezone())
                .profileImage(user.getProfileImage())
                .avatarName(user.getAvatarName())
                .bio(user.getBio())
                .notificationsEnabled(user.isNotificationsEnabled())
                .online(user.isOnline())
                .build();
    }
}
