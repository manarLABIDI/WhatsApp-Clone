package com.mlsoftware.whatsappclone.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageRequest {

    private String content;
    private String senderId;
    private String receiverId;
    private MessageType type;
    private String chatId;
}
