package com.mlsoftware.whatsappclone.notification;


import com.mlsoftware.whatsappclone.message.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {

    private String chatId;
    private String content;
    private String senderId;
    private String receiverId;
    private String chatName;
    private MessageType messageType;
    private NotificationType type;
    private byte[] media;
}
