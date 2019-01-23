package am.chat.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class MessagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Conversation_id")
    private int conversationId;

    @Column(name = "message_from_id")
    private int messageFromId;

    @Column(name = "message_to_id")
    private int messageToId;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="send_date", nullable = false,
            columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private Date registerDate = new Date();

    @ManyToOne
    @JoinColumn(name = "message_from_id", referencedColumnName = "user_id",
            insertable = false,updatable = false)
    private UsersEntity usersMessageFromEntity;

    @ManyToOne
    @JoinColumn(name = "message_to_id", referencedColumnName = "user_id",
            insertable = false,updatable = false)
    private UsersEntity usersMessageToEntity;

    public MessagesEntity() {

    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getMessageFromId() {
        return messageFromId;
    }

    public void setMessageFromId(int messageFromId) {
        this.messageFromId = messageFromId;
    }

    public int getMessageToId() {
        return messageToId;
    }

    public void setMessageToId(int messageToId) {
        this.messageToId = messageToId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public UsersEntity getUsersMessageFromEntity() {
        return usersMessageFromEntity;
    }

    public void setUsersMessageFromEntity(UsersEntity usersMessageFromEntity) {
        this.usersMessageFromEntity = usersMessageFromEntity;
    }

    public UsersEntity getUsersMessageToEntity() {
        return usersMessageToEntity;
    }

    public void setUsersMessageToEntity(UsersEntity usersMessageToEntity) {
        this.usersMessageToEntity = usersMessageToEntity;
    }

}
