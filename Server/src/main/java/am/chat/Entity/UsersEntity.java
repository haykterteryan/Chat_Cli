package am.chat.Entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_login",unique = true,nullable = false)
    private String login;

    @Column(name = "user_password",nullable = false)
    private String password;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="register_date", nullable = false,
            columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private Date registerDate = new Date();

    @OneToMany(mappedBy = "usersEntity")
    private List<FriendsEntity> user;

    @OneToMany(mappedBy = "friendusersEntity")
    private List<FriendsEntity> friend;

    @OneToMany(mappedBy = "fromRequestEntity")
    private List<FriendRequestEntity> fromRequest;

    @OneToMany(mappedBy = "toRequestEntity")
    private List<FriendRequestEntity> toRequest;

    @OneToMany(mappedBy = "usersMessageFromEntity")
    private List<MessagesEntity> fromMessageRequest;

    @OneToMany(mappedBy = "usersMessageToEntity")
    private List<MessagesEntity> toMessageRequest;

    public UsersEntity(){

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public List<FriendsEntity> getUser() {
        return user;
    }

    public void setUser(List<FriendsEntity> user) {
        this.user = user;
    }

    public List<FriendsEntity> getFriend() {
        return friend;
    }

    public void setFriend(List<FriendsEntity> friend) {
        this.friend = friend;
    }

    @Override
    public String toString() {
        return "UsersEntity{" +
                "userId=" + userId +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", registerDate='" + registerDate + '\'' +
                '}';
    }
}

