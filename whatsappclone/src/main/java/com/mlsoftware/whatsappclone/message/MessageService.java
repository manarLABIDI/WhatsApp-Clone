package com.mlsoftware.whatsappclone.message;

import com.mlsoftware.whatsappclone.chat.Chat;
import com.mlsoftware.whatsappclone.chat.ChatRepository;
import com.mlsoftware.whatsappclone.file.FileService;
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

    public void saveMessage(MessageRequest messageRequest){
         Chat c  = chatRepository.findById(messageRequest.getChatId())
                 .orElseThrow(() -> new EntityNotFoundException("Chat not found"));
         Message m = new Message();
         m.setChat(c);
         m.setContent(messageRequest.getContent());
         m.setSenderId(messageRequest.getSenderId());
         m.setReceiverId(messageRequest.getReceiverId());
         m.setType(messageRequest.getType());
         m.setState(MessageState.SENT);

         messageRepository.save(m);

        //todo notification
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
        //final String recipientId = getRecipientId(chat, authentication);

        messageRepository.setMessagesToSeenByChatId(chatId, MessageState.SEEN);
        // todo notification
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
