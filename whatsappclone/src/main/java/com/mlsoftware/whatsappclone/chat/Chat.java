package com.mlsoftware.whatsappclone.chat;

import com.mlsoftware.whatsappclone.common.BaseAuditingEntity;
import com.mlsoftware.whatsappclone.message.Message;
import com.mlsoftware.whatsappclone.message.MessageState;
import com.mlsoftware.whatsappclone.message.MessageType;
import com.mlsoftware.whatsappclone.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="chat")
public class Chat  extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name = "sender_Id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "recipient_Id")
    private User recipient;
    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @OrderBy("createdDate DESC")
    private List<Message> messages;

    @Transient
    public String getChatName(final String senderId){

        if(recipient.getId().equals(senderId)){
            return sender.getFirstname() + " "+ sender.getLastname();
        }
        return recipient.getFirstname() + " "+ recipient.getLastname();

    }

    @Transient
    public long getUnreadMessages(final String senderId){
        return messages
                .stream()
                .filter(m -> m.getReceiverId().equals(senderId))
                .filter( m -> MessageState.SENT == m.getState())
                .count();
    }

    @Transient
    public String getLastMessage(){
        if(messages !=null && !messages.isEmpty()){
          if(messages.get(0).getType() != MessageType.TEXT){
              return "Attachment";

          }
          return  messages.get(0).getContent();
        }
        return  null;
    }

    @Transient
    public LocalDateTime getLastMessageTime(){
        if(messages !=null && !messages.isEmpty()){
            messages.get(0).getCreatedDate();
        }
        return  null;
    }
}
