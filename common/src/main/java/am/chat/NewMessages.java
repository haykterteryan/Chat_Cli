package am.chat;

import java.io.Serializable;

import java.util.Date;


public class NewMessages implements Serializable {
    private int messagesFromId;
    private int messageToId;
    private String userName;
    private String friendName;
    private String message;
    private Date date;

    public NewMessages(int messagesFromId, String message, Date dateTime) {
        this.messagesFromId = messagesFromId;
        this.message = message;
        this.date = dateTime;
    }

    public NewMessages(int messagesFromId, int messageToId, String userName, String friendName, String message, Date date) {
        this.messagesFromId = messagesFromId;
        this.messageToId = messageToId;
        this.userName = userName;
        this.friendName = friendName;
        this.message = message;
        this.date = date;
    }


    public int getMessagesFromId() {
        return messagesFromId;
    }

    public void setMessagesFromId(int messagesFromId) {
        this.messagesFromId = messagesFromId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        message = message;
    }

    public Date getDateTime() {
        return date;
    }

    public void setDateTime(Date dateTime) {
        this.date = dateTime;
    }

    public int getMessageToId() {
        return messageToId;
    }

    public void setMessageToId(int messageToId) {
        this.messageToId = messageToId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "NewMessages{" +
                "messagesFromId=" + messagesFromId +
                ", messageToId=" + messageToId +
                ", userName='" + userName + '\'' +
                ", friendName='" + friendName + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
