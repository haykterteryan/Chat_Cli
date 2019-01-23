package am.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiImpl extends Remote {

    boolean register(String login, String password, String firstName, String lastName) throws RemoteException;

    User login(String login, String password) throws RemoteException;

    List<User> search(String userName) throws RemoteException;

    List<User> showFriends(int userId) throws RemoteException;

    List<NewMessages> checkForNewMessages(int userId) throws RemoteException;

    List<NewMessages> showMessageHistory(int userId, int frinedId) throws  RemoteException;

    void sendFriendRequestByUserId(int id, String firstName, String lastName, int frinedId) throws RemoteException;

    List<Requests> showFriendsRequest(int userId) throws RemoteException;

    void accetOrIgnorFriendRequest(int userId, int requestId, int requestCommand) throws RemoteException;

}
