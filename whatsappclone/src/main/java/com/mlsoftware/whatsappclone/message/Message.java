package com.mlsoftware.whatsappclone.message;

import com.mlsoftware.whatsappclone.chat.Chat;
import com.mlsoftware.whatsappclone.common.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="messages")
public class Message extends BaseAuditingEntity {

    @Id
    @SequenceGenerator(name = "msg_seq",sequenceName = "msq_seq", allocationSize= 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msq_seq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
    @Enumerated(EnumType.STRING)
    private MessageState state;
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "sender", nullable = false)
    private String senderId;
    @Column(name = "receiver", nullable = false)
    private String receiverId;
}
