package com.bytechat.service;

import com.bytechat.dto.MessageDto;
import com.bytechat.dto.SendMessageRequest;
import com.bytechat.model.Message;
import com.bytechat.repository.MessageRepository;
import com.bytechat.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.bytechat.dto.SearchResultDto;
import com.bytechat.model.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<MessageDto> getConversation(String userId1, String userId2) {
        if (!userRepository.existsById(userId1) || !userRepository.existsById(userId2)) {
            throw new IllegalArgumentException("User(s) not found");
        }

        List<Message> messages = messageRepository.findConversation(userId1, userId2);
        
        messages.sort(Comparator.comparing(Message::getCreatedAt));
        
        return messages.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public MessageDto saveMessage(SendMessageRequest request, String senderId) {
        if (!userRepository.existsById(senderId)) {
            throw new IllegalArgumentException("Sender not found");
        }
        
        if (!userRepository.existsById(request.getReceiverId())) {
            throw new IllegalArgumentException("Receiver not found");
        }
        
        Message message = Message.builder()
                .senderId(senderId)
                .receiverId(request.getReceiverId())
                .message(request.getMessage())
                .messageType(request.getMessageType())
                .fileUrl(request.getFileUrl())
                .createdAt(Instant.now())
                .readStatus(false)
                .build();
                
        Message savedMessage = messageRepository.save(message);
        return mapToDto(savedMessage);
    }
    
    public void markAsRead(String messageId, String receiverId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
                
        if (!message.getReceiverId().equals(receiverId)) {
            throw new IllegalArgumentException("Not authorized to mark this message as read");
        }
        
        message.setReadStatus(true);
        messageRepository.save(message);
    }

    public void markConversationAsRead(String senderId, String receiverId) {
        List<Message> unreadMessages = messageRepository.findBySenderIdAndReceiverIdAndReadStatusFalse(senderId, receiverId);
        if (!unreadMessages.isEmpty()) {
            unreadMessages.forEach(msg -> msg.setReadStatus(true));
            messageRepository.saveAll(unreadMessages);
        }
    }

    public List<SearchResultDto> searchMessages(String query, String userId) {
        List<Message> messages = messageRepository.searchMessages(query, userId);
        List<SearchResultDto> results = new ArrayList<>();
        
        for (Message msg : messages) {
            String otherUserId = msg.getSenderId().equals(userId) ? msg.getReceiverId() : msg.getSenderId();
            Optional<User> otherUser = userRepository.findById(otherUserId);
            
            results.add(SearchResultDto.builder()
                    .message(mapToDto(msg))
                    .contactId(otherUserId)
                    .contactName(otherUser.map(User::getFullName).orElse("Unknown User"))
                    .contactProfileImage(otherUser.map(User::getProfileImage).orElse(null))
                    .build());
        }
        
        return results;
    }

    private MessageDto mapToDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .message(message.getMessage())
                .messageType(message.getMessageType())
                .fileUrl(message.getFileUrl())
                .createdAt(message.getCreatedAt())
                .readStatus(message.isReadStatus())
                .build();
    }
}
