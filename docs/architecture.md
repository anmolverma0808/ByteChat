# ByteChat Architecture

## High Level Architecture

Angular Frontend
        |
REST APIs
        |
Spring Boot Backend
        |
MongoDB Database

WebSocket Server
        |
Real-time Messaging

## Communication Flow

User sends message
↓
Angular WebSocket client
↓
Spring Boot WebSocket Controller
↓
Message saved to MongoDB
↓
Message broadcast to receiver
↓
Receiver UI updates instantly