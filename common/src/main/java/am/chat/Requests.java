package am.chat;

import java.io.Serializable;

public class Requests implements Serializable {
    private String userName;
    private int requestId;
    private int requestToId;

    public Requests(String userName, int requestId) {
        this.userName = userName;
        this.requestId = requestId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRequestFromId() {
        return requestId;
    }

    public void setRequestFromId(int requestFromId) {
        this.requestId = requestFromId;
    }

    public int getRequestToId() {
        return requestToId;
    }

    public void setRequestToId(int requestToId) {
        this.requestToId = requestToId;
    }

    @Override
    public String toString() {
        return "Requests{" +
                "userName='" + userName + '\'' +
                ", requestId=" + requestId +
                ", requestToId=" + requestToId +
                '}';
    }
}

