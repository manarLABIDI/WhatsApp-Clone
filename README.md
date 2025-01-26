# WhatsApp-Like Messaging Application

This project is a messaging application designed to mimic the core features of WhatsApp, including real-time chat, media sharing, and notification handling. It is built with Angular for the front-end and a Spring Boot backend, providing WebSocket support for real-time communication.

## Features

- **Real-time Messaging**: Users can send and receive text and image messages in real-time using WebSocket connections.
- **User Authentication**: Integrated with Keycloak for secure login and account management.
- **Media Sharing**: Users can send images as attachments during conversations.
- **Chat Management**: Users can select and view different chats, with unread message count management.
- **Notification Handling**: Real-time notifications for new messages or status changes within a chat.

## Tech Stack

- **Frontend**:
    - Angular
    - WebSocket (using `SockJS` and `Stomp.js`)
    - Bootstrap (for UI components)

- **Backend**:
    - Spring Boot
    - WebSocket for real-time communication

- **Authentication**:
    - Keycloak (OAuth2 Authentication)

- **WebSocket Communication**:
    - SockJS
    - STOMP (Simple Text Oriented Messaging Protocol)