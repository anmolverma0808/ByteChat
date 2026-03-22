package com.bytechat.controller;

import com.bytechat.dto.ApiResponse;
import com.bytechat.dto.MessageDto;
import com.bytechat.dto.ReadReceiptRequest;
import com.bytechat.dto.SendMessageRequest;
import com.bytechat.model.User;
import com.bytechat.security.CustomUserDetails;
import com.bytechat.repository.UserRepository;
import com.bytechat.service.ContactService;
import com.bytechat.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import com.bytechat.dto.SearchResultDto;
import com.bytechat.dto.TypingRequest;

@RestController
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final ContactService contactService;

    public ChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate, UserRepository userRepository, ContactService contactService) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
        this.contactService = contactService;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {
        String senderEmail = null;
        Object userPrincipal = headerAccessor != null ? headerAccessor.getUser() : null;
        if (userPrincipal != null) {
            senderEmail = ((Principal) userPrincipal).getName();
        } else {
            log.error("Unauthenticated socket message attempt");
            return;
        }

        User sender = userRepository.findByEmail(senderEmail).orElse(null);
        if (sender == null) return;
        
        try {
            contactService.ensureContactExists(sender.getId(), request.getReceiverId());
            
            MessageDto savedMessage = messageService.saveMessage(request, sender.getId());

            // Get receiver email for WebSocket delivery
            User receiver = userRepository.findById(request.getReceiverId()).orElse(null);
            if (receiver != null) {
                messagingTemplate.convertAndSendToUser(
                        receiver.getEmail().trim(),
                        "/queue/messages",
                        savedMessage
                );
            }
            
            messagingTemplate.convertAndSendToUser(
                    senderEmail.trim(),
                    "/queue/messages",
                    savedMessage
            );
        } catch (Exception e) {
            log.error("Error saving/sending message", e);
        }
    }

    @MessageMapping("/chat.read")
    public void sendMessageRead(@Payload ReadReceiptRequest request, SimpMessageHeaderAccessor headerAccessor) {
        String receiverEmail = null;
        Object userPrincipal = headerAccessor.getUser();
        if (userPrincipal != null) {
            receiverEmail = ((Principal) userPrincipal).getName();
        } else {
            return;
        }

        User receiver = userRepository.findByEmail(receiverEmail).orElse(null);
        if (receiver == null) return;

        try {
            messageService.markConversationAsRead(request.getSenderId(), receiver.getId());

            // Notify the original sender that their messages were read
            User sender = userRepository.findById(request.getSenderId()).orElse(null);
            if (sender != null) {
                request.setReaderId(receiver.getId()); // Set who read it
                messagingTemplate.convertAndSendToUser(
                        sender.getEmail().trim(),
                        "/queue/read-receipts",
                        request
                );
            }
        } catch (Exception e) {
            log.error("Error updating read status", e);
        }
    }

    @MessageMapping("/chat.typing")
    public void sendMessageTyping(@Payload TypingRequest request, SimpMessageHeaderAccessor headerAccessor) {
        String senderEmail = null;
        Object userPrincipal = headerAccessor.getUser();
        if (userPrincipal != null) {
            senderEmail = ((Principal) userPrincipal).getName();
        } else {
            return;
        }

        User sender = userRepository.findByEmail(senderEmail).orElse(null);
        if (sender == null) return;

        User receiver = userRepository.findById(request.getReceiverId()).orElse(null);
        if (receiver != null) {
            request.setSenderId(sender.getId());
            messagingTemplate.convertAndSendToUser(
                    receiver.getEmail().trim(),
                    "/queue/typing",
                    request
            );
        }
    }

    @GetMapping("/api/messages/{contactId}")
    public ResponseEntity<ApiResponse<List<MessageDto>>> getConversation(
            Authentication authentication,
            @PathVariable String contactId) {
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<MessageDto> messages = messageService.getConversation(userDetails.getId(), contactId);
        
        return ResponseEntity.ok(ApiResponse.success("Messages fetched successfully", messages));
    }

    @GetMapping("/api/messages/search")
    public ResponseEntity<ApiResponse<List<SearchResultDto>>> searchMessages(
            Authentication authentication,
            @RequestParam String query) {
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<SearchResultDto> results = messageService.searchMessages(query, userDetails.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Search completed", results));
    }
}
