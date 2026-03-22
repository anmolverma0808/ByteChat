package com.bytechat.controller;

import com.bytechat.dto.AddContactRequest;
import com.bytechat.dto.ApiResponse;
import com.bytechat.dto.ContactDto;
import com.bytechat.security.CustomUserDetails;
import com.bytechat.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;
    
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContacts(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<ContactDto> contacts = contactService.getContacts(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Contacts fetched successfully", contacts));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ContactDto>> addContact(
            Authentication authentication,
            @Valid @RequestBody AddContactRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        ContactDto contact = contactService.addContact(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Contact added successfully", contact));
    }

    @DeleteMapping("/{contactUserId}")
    public ResponseEntity<ApiResponse<Void>> removeContact(
            Authentication authentication,
            @PathVariable String contactUserId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        contactService.removeContact(userDetails.getId(), contactUserId);
        return ResponseEntity.ok(ApiResponse.success("Contact removed successfully", null));
    }

    @GetMapping("/{contactUserId}")
    public ResponseEntity<ApiResponse<ContactDto>> getContact(
            Authentication authentication,
            @PathVariable String contactUserId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        ContactDto contact = contactService.getContact(userDetails.getId(), contactUserId);
        return ResponseEntity.ok(ApiResponse.success("Contact fetched successfully", contact));
    }
}
