# WebSocket Flow

Protocol: STOMP over WebSocket

Endpoint:
/ws-chat

Topics:
/topic/messages

Queue:
/user/queue/messages

Flow

Sender sends message
↓
Angular sends STOMP message
↓
Spring Boot receives message
↓
Message stored in MongoDB
↓
Server sends message to receiver topic
↓
Receiver Angular client updates chat