package com.mlsoftware.whatsappclone.message;

import com.mlsoftware.whatsappclone.chat.Chat;
import com.mlsoftware.whatsappclone.chat.ChatRepository;
import com.mlsoftware.whatsappclone.file.FileService;
import com.mlsoftware.whatsappclone.file.FileUtils;
import com.mlsoftware.whatsappclone.notification.Notification;
import com.mlsoftware.whatsappclone.notification.NotificationService;
import com.mlsoftware.whatsappclone.notification.NotificationType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper mapper;
    private final FileService fileService;
    private final NotificationService notificationService;

    public void saveMessage(MessageRequest messageRequest){
         Chat chat  = chatRepository.findById(messageRequest.getChatId())
                 .orElseThrow(() -> new EntityNotFoundException("Chat not found"));
         Message m = new Message();
         m.setChat(chat);
         m.setContent(messageRequest.getContent());
         m.setSenderId(messageRequest.getSenderId());
         m.setReceiverId(messageRequest.getReceiverId());
         m.setType(messageRequest.getType());
         m.setState(MessageState.SENT);

         messageRepository.save(m);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .messageType(messageRequest.getType())
                .content(messageRequest.getContent())
                .senderId(messageRequest.getSenderId())
                .receiverId(messageRequest.getReceiverId())
                .type(NotificationType.MESSAGE)
                .chatName(chat.getTargetChatName(m.getSenderId()))
                .build();

        notificationService.sendNotification(m.getReceiverId(), notification);

    }

    public List<MessageResponse> findChatMessages(String chatId) {

        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(mapper::toMessageResponse)
                .toList();

    }
    @Transactional
    public void setMessagesToSeen(String chatId, Authentication authentication){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("chat not found"));
        final String recipientId = getRecipientId(chat, authentication);

        messageRepository.setMessagesToSeenByChatId(chatId, MessageState.SEEN);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .type(NotificationType.SEEN)
                .senderId(getSenderId(chat, authentication))
                .receiverId(recipientId)
                .build();

        notificationService.sendNotification(recipientId, notification);
    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("chat not found"));
        final String senderId = getSenderId(chat, authentication);
        final String receiverId = getRecipientId(chat, authentication);

        final String filePath = fileService.saveFile(file, senderId);
        Message message = new Message();
        message.setReceiverId(receiverId);
        message.setSenderId(senderId);
        message.setState(MessageState.SENT);
        message.setType(MessageType.IMAGE);
        message.setMediaFilePath(filePath);
        message.setChat(chat);
        messageRepository.save(message);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .type(NotificationType.IMAGE)
                .messageType(MessageType.IMAGE)
                .senderId(senderId)
                .receiverId(receiverId)
                .media(FileUtils.readFileFromLocation(filePath))
                .build();

        notificationService.sendNotification(receiverId, notification);


    }

    private String getRecipientId(Chat chat, Authentication authentication) {

        if(chat.getSender().getId().equals(authentication.getName())){
            return chat.getRecipient().getId();
        }
        return chat.getSender().getId();
    }
    private String getSenderId(Chat chat, Authentication authentication) {

        if(chat.getSender().getId().equals(authentication.getName())){
            return chat.getSender().getId();
        }
        return chat.getRecipient().getId();
    }
}
