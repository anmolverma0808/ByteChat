package com.bytechat.controller;

import com.bytechat.dto.ApiResponse;
import com.bytechat.model.FileDocument;
import com.bytechat.model.User;
import com.bytechat.repository.UserRepository;
import com.bytechat.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
 
    private final FileService fileService;
    private final UserRepository userRepository;
 
    public FileController(FileService fileService, UserRepository userRepository) {
        this.fileService = fileService;
        this.userRepository = userRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileDocument>> uploadFile(
            Authentication authentication,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "receiverId", required = false) String receiverId) {
            
        String senderId = getCurrentUserId(authentication);
        FileDocument savedFile = fileService.storeFile(file, senderId, receiverId);
        
        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", savedFile));
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileName,
            @RequestParam(value = "name", required = false) String displayName,
            HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        String finalFileName = (displayName != null && !displayName.isEmpty()) ? displayName : resource.getFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + finalFileName + "\"")
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FileDocument>>> getUserFiles(Authentication authentication) {
        String userId = getCurrentUserId(authentication);
        List<FileDocument> files = fileService.getUserFiles(userId);
        return ResponseEntity.ok(ApiResponse.success("Files fetched successfully", files));
    }

    @PostMapping("/toggle-star/{id}")
    public ResponseEntity<ApiResponse<FileDocument>> toggleStar(@PathVariable String id) {
        FileDocument file = fileService.toggleStar(id);
        return ResponseEntity.ok(ApiResponse.success("File star status updated", file));
    }

    private String getCurrentUserId(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
        return user.getId();
    }
}
