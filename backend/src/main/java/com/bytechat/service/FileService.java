package com.bytechat.service;

import com.bytechat.model.FileDocument;
import com.bytechat.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;
    private final String UPLOAD_DIR = "uploads";
    private final Path fileStorageLocation;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.fileStorageLocation = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public FileDocument storeFile(MultipartFile file, String senderId, String receiverId) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (originalFileName.contains("..")) {
                throw new IllegalArgumentException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            String fileExtension = "";
            if (originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(uniqueFileName)
                    .toUriString();

            FileDocument fileDocument = FileDocument.builder()
                    .senderId(senderId)
                    .receiverId(receiverId)
                    .fileName(originalFileName)
                    .fileUrl(fileDownloadUri)
                    .fileSize(file.getSize())
                    .uploadedAt(Instant.now())
                    .build();

            return fileRepository.save(fileDocument);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    public List<FileDocument> getUserFiles(String userId) {
        return fileRepository.findBySenderIdOrReceiverId(userId);
    }

    public FileDocument toggleStar(String fileId) {
        FileDocument file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: " + fileId));
        file.setStarred(!file.isStarred());
        return fileRepository.save(file);
    }
}
