package com.mlsoftware.whatsappclone.user;

import com.mlsoftware.whatsappclone.chat.Chat;
import com.mlsoftware.whatsappclone.common.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User extends BaseAuditingEntity {

    private static final int LAST_ACTIVE_INTERVAL= 5;

    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDateTime lastSeen;


    @OneToMany(mappedBy = "sender")
    private List<Chat> chatsSender;
    @OneToMany(mappedBy = "recipient")
    private List<Chat> chatsRecipient;

    @Transient
    public boolean isUserOnline() {
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(LAST_ACTIVE_INTERVAL));
    }
}
