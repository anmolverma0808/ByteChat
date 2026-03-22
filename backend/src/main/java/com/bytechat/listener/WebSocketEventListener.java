package com.bytechat.listener;

import com.bytechat.dto.StatusUpdateMessage;
import com.bytechat.model.User;
import com.bytechat.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

@Component
public class WebSocketEventListener {

    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final UserRepository userRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public WebSocketEventListener(UserRepository userRepository, SimpMessageSendingOperations messagingTemplate) {
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = getUserId(headerAccessor);

        if (userId != null) {
            log.info("User connected: {}", userId);
            updateUserStatus(userId, true);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = getUserId(headerAccessor);

        if (userId != null) {
            log.info("User disconnected: {}", userId);
            updateUserStatus(userId, false);
        }
    }

    private String getUserId(StompHeaderAccessor accessor) {
        if (accessor != null && accessor.getUser() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) accessor.getUser();
            if (auth != null && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                // Assuming username is email or similar unique ID
                String email = ((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal()).getUsername();
                Optional<User> user = userRepository.findByEmail(email);
                return user.map(User::getId).orElse(null);
            }
        }
        return null;
    }

    private void updateUserStatus(String userId, boolean online) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setOnline(online);
            userRepository.save(user);
            
            // Broadcast status change
            StatusUpdateMessage statusMessage = new StatusUpdateMessage(userId, online);
            messagingTemplate.convertAndSend("/topic/status", statusMessage);
            log.info("Broadcasted status update for user {}: {}", userId, online ? "ONLINE" : "OFFLINE");
        });
    }
}
