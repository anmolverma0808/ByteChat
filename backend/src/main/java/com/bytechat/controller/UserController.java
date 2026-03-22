package com.bytechat.controller;

import com.bytechat.dto.ApiResponse;
import com.bytechat.dto.UserProfileDto;
import com.bytechat.model.User;
import com.bytechat.repository.UserRepository;
import com.bytechat.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> getProfile(Authentication authentication) {
        String userId = getCurrentUserId(authentication);
        UserProfileDto profile = userService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", profile));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfileDto>>> getAllUsers() {
        List<UserProfileDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", users));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateProfile(
            Authentication authentication,
            @RequestBody UserProfileDto request) {
        String userId = getCurrentUserId(authentication);
        UserProfileDto updatedProfile = userService.updateUserProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updatedProfile));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(Authentication authentication) {
        String userId = getCurrentUserId(authentication);
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully", null));
    }

    private String getCurrentUserId(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
        return user.getId();
    }
}
