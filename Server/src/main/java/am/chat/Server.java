package am.chat;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Server {

  //  private static final SessionFactory sessionfactory = new Configuration().configure().buildSessionFactory();


    static Scanner scanner = new Scanner(System.in);
    private static final Connection connection = getConnection();


    public static void main(String[] args) {

        serverRMI();
        new SocketServer().socketServer();

    }

    private static void serverRMI() {
        System.out.println("Server has started with port 8078");
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(8078);
            registry.rebind("search", new RmiServer());
            registry.rebind("login", new RmiServer());
            registry.rebind("register", new RmiServer());
            registry.rebind("showfriends",new RmiServer());
            registry.rebind("checkForNewMessages", new RmiServer());
            registry.rebind("showmessagehistory", new RmiServer());
            registry.rebind("sendfriendrequestbyuserid", new RmiServer());
            registry.rebind("showfriendsrequest", new RmiServer());
            registry.rebind("accetorignorfriendrequest" , new RmiServer());
        } catch ( RemoteException e ) {
            e.printStackTrace();
        }

    }

    private static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/cli_chat",
                    "root",
                    ""
            );

            System.out.println("Connection successful");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean registerUserInBase(String login, String password, String firstName, String lastName) {
        String query = "Insert into users (user_login,user_password,first_name,last_name) values (?,?,?,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,firstName);
            preparedStatement.setString(4,lastName);
            preparedStatement.executeUpdate();

        } catch ( SQLException e ) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    static User login(String login, String password) {
        String query = "Select * from users where user_login = ? and user_password =?";
        try {
            PreparedStatement preparedstaement = connection.prepareStatement(query);
            preparedstaement.setString(1, login);
            preparedstaement.setString(2, password);
            preparedstaement.execute();

            ResultSet result = preparedstaement.executeQuery();

            if(result.next()){
                int userId = result.getInt("user_id");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                return new User(userId,firstName,lastName);
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    static List<User> search(String userName) {
        List<User> searchResult = new ArrayList<>();
        String query = "Select * from users where first_name =?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.execute();

            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
              int userId = result.getInt("user_id");
              String firstName = result.getString("first_name");
              String lastname = result.getString("last_name");
              searchResult.add(new User(userId,firstName,lastname));
              return searchResult;
            }
        }
        catch ( SQLException e  ){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    static List<User> showFrineds(int userId)
    {
        List<User> friendsList = new ArrayList<>();
        String query = "Select friends.user_id, friends.friend_id, u.first_name, u.last_name, u.user_id from friends " +
                " inner JOIN users u on friends.friend_id = u.user_id " +
                " where friends.user_id = ? ";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,userId);
            ResultSet result = preparedStatement.executeQuery();
            while(result.next()){
                int friendId = result.getInt("friend_id");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                friendsList.add(new User(friendId,firstName,lastName));
                System.out.println(new User(friendId,firstName,lastName));
                return friendsList;
            }


        }
        catch(SQLException e){
            e.printStackTrace();
        return null;
        }
        return null;
    }

    /**
     * Add message row in messages table
     * @param userid User id who send the messages
     * @param recivedUserId User id who received message
     * @param receivedMessage Received message in TEXT type
     * @param readed Flag of message notification
     */
    static void sendMessageToDB(int userid, int recivedUserId, String receivedMessage, boolean readed) {

        String query = "insert into messages(message_from_id, message_to_id,message,readed) values(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, userid);
            preparedStatement.setInt(2, recivedUserId);
            preparedStatement.setString(3, receivedMessage);
            preparedStatement.setBoolean(4, readed);

            preparedStatement.executeUpdate();
        }
        catch ( SQLException e ){
            e.printStackTrace();
        }

    }

    static List<NewMessages> newMessages(int userId) {
        List<NewMessages> newMessages = new ArrayList<>();
        String query = "Select * from messages where message_to_id = ? and readed = false";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int messageFromId = resultSet.getInt("message_from_id");
                String message = resultSet.getString("message");
                Date sendDate = resultSet.getTimestamp("send_date");

                newMessages.add(new NewMessages(messageFromId,message,sendDate));

            }
        }
        catch ( SQLException e){
            e.printStackTrace();
        }
        markAsReaded(userId);
        return  newMessages;
    }

    /**
     * mark messages as readed updating massages table sets readed column true
     * @param userId who read message
     */
    private static void markAsReaded(int userId) {
        String updateQuery = "UPDATE messages SET readed = true where message_to_id = ? and readed = false";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1,userId);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * show message History  by user and friend Id
     * @param fromId
     * @param toId
     * @return List of messages
     */
    static List<NewMessages> showMessageHistory(int fromId, int toId) {
        List<NewMessages> messagesList = new ArrayList<>();
        String query = "SELECT messages.message, u.user_id, u2.user_id ,u.first_name ,u2.first_name,messages.send_date from messages " +
                " Inner join users u on messages.message_from_id = u.user_id " +
                " inner join users u2 on messages.message_to_id = u2.user_id " +
                "where (u.user_id =? and u2.user_id=?) or (u.user_id =? and u2.user_id=?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,fromId);
            preparedStatement.setInt(2,toId);
            preparedStatement.setInt(3,toId);
            preparedStatement.setInt(4,fromId);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String message =resultSet.getString(1);
                int userId = resultSet.getInt(2);
                int friendId = resultSet.getInt(3);
                String friendName = resultSet.getString(4);
                String userName = resultSet.getString(5);
                Date date = resultSet.getTimestamp(6);
                messagesList.add(new NewMessages(userId,friendId,userName,friendName,message,date));


            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return messagesList;
    }


    static void sendFriendRequestByUserId(int id, int friendId ) {
        String query = "Insert into friend_request (request_from_id, request_to_id,readed) values (?,?,false)";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            preparedStatement.setInt(2,friendId);


            preparedStatement.executeUpdate();


        }catch(SQLException e){
            e.printStackTrace();

        }

    }

    /**
     * Checking for friendship -> check in DB
     * friends request column approved, if it is true returns true;
     * @param userid  user id who want to write
     * @param recivedUserId user id to send message
     * @return
     */

    static boolean checkfriendship(int userid, int recivedUserId) {

        String query = "Select * from friend_request where (request_to_id = ? and request_from_id = ? and aproved =true ) or " +
                "(request_to_id = ? and request_from_id = ? and aproved = true )";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,userid);
            preparedStatement.setInt(2,recivedUserId);
            preparedStatement.setInt(3,recivedUserId);
            preparedStatement.setInt(4,userid);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                System.out.println("lava");
                return true;
            }

        }
        catch ( SQLException e ){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Search friend request by userId in friend request table
     * @param userId
     * @return List of friend requests to pass client;
     */
    static List<Requests> showFrinedsRequest(int userId) {
        List<Requests> requests = new ArrayList<>();
        String query = "Select friend_request.*, u.first_name from friend_request " +
                " inner join users u on friend_request.request_from_id = u.user_id" +
                " where request_to_id = ? and readed = false";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int requestId = resultSet.getInt("friend_request_id");
                String firstName = resultSet.getString("first_name");
                System.out.println(requestId + " "+ firstName);
                requests.add(new Requests(firstName,requestId));

            }

        }catch(SQLException e ){
            e.printStackTrace();
            return null;

        }
        return requests;
    }

    /**
     * accept or ignor friend request
     * @param userId userid how have friend request
     * @param requestId requests id unique number
     * @param requestCommand if command is 1 friend request is accepted and if 0 denided
     */
    static void accetOrIgnorFriendRequest(int userId, int requestId, int requestCommand) {
        String query = "UPDATE friend_request SET aproved = ?, readed = 1 " +
                " WHERE friend_request_id = ? and request_to_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,requestCommand);
            preparedStatement.setInt(2,requestId);
            preparedStatement.setInt(3,userId);

            preparedStatement.executeUpdate();

        }
        catch ( SQLException e ){
            e.printStackTrace();
        }
    }

    static void addToFriendsList(int userId, int requestId) {
        String query = "select request_from_id  from friend_request where friend_request_id =?";


        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,requestId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int requestFromId = resultSet.getInt("request_from_id");
                addToFriends(userId, requestFromId);
                addToFriends(requestFromId,userId);

            }

        }catch ( SQLException e ){
            e.printStackTrace();
        }

    }

    private static void addToFriends(int userId, int requestFromId) {
        String query = "Insert into friends (user_id, friend_id) VALUES (?,?)";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,requestFromId);
            preparedStatement.executeUpdate();

        }
        catch ( SQLException e ){
            e.printStackTrace();
        }
    }
//    static String login(String login, String password) {
//        Session session = sessionfactory.openSession();
//        TypedQuery<UsersEntity> query = session.createQuery("from UsersEntity u where u.login = :login and u.password = :password", UsersEntity.class);
//        query.setParameter("login", login);
//        query.setParameter("password", password);
//        if(query.getSingleResult() !=null) {
//            UsersEntity loggedInUser = query.getSingleResult();
//
//            return loggedInUser.getUserId()+" "+ loggedInUser.getFirstName();
//
//        }
//        return null;
//
//    }
//
//    static void registerUserInBase(String login, String password, String firstName, String lastName) {
//
//        Session session = sessionfactory.openSession();
//
//        UsersEntity user = new UsersEntity();
//        user.setLogin(login);
//        user.setPassword(password);
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//
//        Transaction transaction = session.beginTransaction();
//        session.save(user);
//        transaction.commit();
//
//        session.close();
//
//    }
//
//    static void search(String userFirstName) {
//        System.out.println("Search point " + userFirstName);
//        Session session = sessionfactory.openSession();
//        Query<UsersEntity> query = session.createQuery("from UsersEntity u where u.firstName = :firstname ", UsersEntity.class);
//        query.setParameter("firstname", userFirstName);
//
//        List<UsersEntity> usersEntityList = query.getResultList();
//
//        for (UsersEntity usersEntity : usersEntityList
//                ) {
//            System.out.println(usersEntity);
//        }
//    }
}
