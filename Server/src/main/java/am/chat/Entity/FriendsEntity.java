package am.chat.Entity;


import javax.persistence.*;

@Entity
@Table(name = "friends")
public class FriendsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friends_id")
    private int friendsId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "friend_id", nullable = false)
    private int friendId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id",
            insertable = false,updatable = false)
    private UsersEntity usersEntity;

    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "user_id",
            insertable = false,updatable = false)
    private UsersEntity friendusersEntity;

    public FriendsEntity() {
    }

    public int getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(int friendsId) {
        this.friendsId = friendsId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public UsersEntity getUsersEntity() {
        return usersEntity;
    }

    public void setUsersEntity(UsersEntity usersEntity) {
        this.usersEntity = usersEntity;
    }

    public UsersEntity getFriendusersEntity() {
        return friendusersEntity;
    }

    public void setFriendusersEntity(UsersEntity friendusersEntity) {
        this.friendusersEntity = friendusersEntity;
    }
}
