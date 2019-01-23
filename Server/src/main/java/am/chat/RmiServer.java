package am.chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiServer extends UnicastRemoteObject implements RmiImpl {
    RmiServer() throws RemoteException {

    }


    @Override
    public boolean register(String login, String password, String firstName, String lastName) throws RemoteException {

        return Server.registerUserInBase(login,password, firstName , lastName);

    }

    @Override
    public User login(String login, String password) throws RemoteException {

        return Server.login(login,password);
    }

    @Override
    public List<User> search(String userName) throws RemoteException {
        System.out.println(Server.search(userName));
       return Server.search(userName);
    }

    @Override
    public List<User> showFriends(int userId) throws RemoteException {

        return Server.showFrineds(userId);
    }

    @Override
    public List<NewMessages> checkForNewMessages(int userId) throws RemoteException {

        return Server.newMessages(userId);
    }

    @Override
    public List<NewMessages> showMessageHistory(int userId, int frinedId) throws RemoteException {
        return Server.showMessageHistory(userId,frinedId);
    }

    @Override
    public void sendFriendRequestByUserId(int id, String firstName, String lastName, int frinedId) throws RemoteException {
        SocketServer.sendFriendRequest(id,firstName,lastName,frinedId);
        Server.sendFriendRequestByUserId(id,frinedId);
    }

    @Override
    public List<Requests> showFriendsRequest(int userId) throws RemoteException {

       return Server.showFrinedsRequest(userId);
    }

    @Override
    public void accetOrIgnorFriendRequest(int userId, int requestId, int requestCommand) throws RemoteException {
        if(requestCommand ==1 ) {
            Server.addToFriendsList(userId,requestId);
        }

        Server.accetOrIgnorFriendRequest(userId,requestId,requestCommand);
    }


}
