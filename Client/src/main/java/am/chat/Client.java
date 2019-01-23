package am.chat;


import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    static Scanner scanner = new Scanner(System.in);
    static User loggedInUser = null;
    static Socket socket;
    static Thread listenThread;
    static InputStream inputStream;


    public static void main(String[] args) {

        while(true){

            if(loggedInUser == null){
                System.out.println("1. Login ");
                System.out.println("2. Register");
                String command = scanner.nextLine();
                switch (command) {
                    case "1":
                        login();
                        break;
                    case "2":
                        register();
                        break;
                }
            }
            if(loggedInUser != null){
                System.out.println("\t\tWelcome to Cli_chat " + loggedInUser.getFirstName()+" "+ loggedInUser.getLastName());
                System.out.println("1.Friends");
                System.out.println("2.Write Messeges");
                System.out.println("3.Show Message History");
                System.out.println("4.Search users");
                System.out.println("5.Show Friend request");
                System.out.println("6.Send friend request by user id");
                System.out.println("7.Accept or ignore friend request");
                System.out.println("8.Log out");
                checkForMessage();
                String command = scanner.nextLine();


                switch (command) {
                    case "1":
                        showFriends();
                        break;
                    case "2":
                        writeMesseges();
                        break;
                    case"3":
                        showMessageHistory();
                        break;
                    case "4":
                        searchFriends();
                        break;
                    case "5":
                        showFriendsRequest();
                        break;
                    case "6":
                        sendFriendRequestByUserId();
                        break;
                    case "7":
                        acceptOrIgnoreFriendRequest();
                        break;
                    case "8":
                        logout();
                        break;
                }

            }
        }
    }

    private static void acceptOrIgnoreFriendRequest() {
        System.out.println("Enter friend request id");
        int requestId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter 1 to accept or Enter 0 to denied");
        int requestCommand  = Integer.parseInt(scanner.nextLine());
        RmiImpl rmiImpl = null;
        try {
            rmiImpl = (RmiImpl) Naming.lookup("rmi://127.0.0.1:8078/accetorignorfriendrequest");
            rmiImpl.accetOrIgnorFriendRequest(loggedInUser.getUserId(),requestId,requestCommand);

        } catch ( NotBoundException e ) {
            e.printStackTrace();
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        } catch ( RemoteException e ) {
            e.printStackTrace();
        }
    }

    private static void sendFriendRequestByUserId() {
        System.out.println("Enter user id to sent friend request");
        int frinedId = Integer.parseInt(scanner.nextLine());
        RmiImpl rmiImpl = null;
        try {
            rmiImpl = (RmiImpl) Naming.lookup("rmi://127.0.0.1:8078/sendfriendrequestbyuserid");
            rmiImpl.sendFriendRequestByUserId(loggedInUser.getUserId(),loggedInUser.getFirstName(),loggedInUser.getLastName(),frinedId);


        } catch ( NotBoundException e ) {
            e.printStackTrace();
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        } catch ( RemoteException e ) {
            e.printStackTrace();
        }
    }

    private static void showMessageHistory() {
        List<NewMessages> messagesHistory ;
        System.out.println("Enter user id to show message history with him");
        int frinedId = Integer.parseInt(scanner.nextLine());
        RmiImpl rmiImpl = null;
        try {
            rmiImpl = (RmiImpl) Naming.lookup("rmi://127.0.0.1:8078/showmessagehistory");
            messagesHistory = rmiImpl.showMessageHistory(loggedInUser.getUserId(),frinedId);

            for (NewMessages messages: messagesHistory
                    ) {
                if(messages.getMessagesFromId() == loggedInUser.getUserId()){
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t" + messages.getMessage());
                }else{
                    System.out.println(messages.getFriendName()+": "+ messages.getMessage());
                }
            }
        } catch ( NotBoundException e ) {
            e.printStackTrace();
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        } catch ( RemoteException e ) {
            e.printStackTrace();
        }

    }

    private static void checkForMessage() {
        List<NewMessages> newMessages;
        RmiImpl rmiImpl = null;
        try {
            rmiImpl = (RmiImpl) Naming.lookup("rmi://127.0.0.1:8078/checkForNewMessages");
            newMessages = rmiImpl.checkForNewMessages(loggedInUser.getUserId());

            for (NewMessages messages: newMessages
                 ) {
                System.out.println(messages);

            }
        } catch ( NotBoundException e ) {
            e.printStackTrace();
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        } catch ( RemoteException e ) {
            e.printStackTrace();
        }
    }

    private static void listenToServer() {

        listenThread = new Thread(()->{
            while(true) {
                try {
                    inputStream = socket.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String listen = bufferedReader.readLine();

                    if (listen.equals("%1close1%")) {
                        break;
                    }
                    if (listen.substring(0, 5).equals("$frf$")) {
                        System.out.println(listen.substring(5));
                        continue;
                    } else {
                        System.out.println(listen);
                    }
                } catch ( IOException e ) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        listenThread.start();

    }

    private static void showFriends() {
        List<User> friendsList = new ArrayList<>();
        try {
            RmiImpl rmiImpl = (RmiImpl) Naming.lookup("rmi://127.0.0.1:8078/showfriends");
            friendsList = rmiImpl.showFriends(loggedInUser.getUserId());
            if(friendsList == null) {
                System.out.println("Friennds not found");
                return;}
            for (User friend : friendsList
                    ) {
                System.out.println(friend);
            }

        } catch ( NotBoundException e ) {
            e.printStackTrace();
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        } catch ( RemoteException e ) {
            e.printStackTrace();
        }

    }

    private static void writeMesseges() {
        System.out.println("Enter user id hom you want to write");
        Scanner messageScanner = new Scanner(System.in);
                OutputStream outputStream = null;
                try {
                    outputStream = socket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    printWriter.println(messageScanner.nextLine());
                    System.out.println("enter message");
                    printWriter.println(messageScanner.nextLine());
                } catch ( IOException e ) {
                    e.printStackTrace();
                }


    }

    private static void searchFriends() {
        List<User> searchResult = new ArrayList<>();
        System.out.print("Enter user First Name:");
        String firstName = scanner.nextLine();
        try {
            RmiImpl rmiImpl = (RmiImpl) Naming.lookup("rmi://127.0.0.1:8078/search");
            searchResult = rmiImpl.search(firstName);
            if(searchResult == null) {
                System.out.println("Users not found");
                return;}
            for (User user: searchResult
                 ) {
                System.out.println(user);
            }

        } catch ( NotBoundException e ) {
            e.printStackTrace();
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        } catch ( RemoteException e ) {
            e.printStackTrace();
        }
    }

    private static void showFriendsRequest() {
        List<Requests> friendsRequest = new ArrayList<>();
        try {
            RmiImpl rmiImpl = (RmiImpl) Naming.lookup("rmi://127.0.0.1:8078/showfriendsrequest");
            friendsRequest = rmiImpl.showFriendsRequest(loggedInUser.getUserId());
            if(friendsRequest.isEmpty()){
                System.out.println("you have not any request");
                return;
            }
            for (Requests request : friendsRequest
                 ) {
                System.out.println(request);
            }


        } catch ( NotBoundException e ) {
            e.printStackTrace();
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        } catch ( RemoteException e ) {
            e.printStackTrace();
        }
    }

    /**
     * logout
     */
    private static void logout() {
        loggedInUser = null;
        OutputStream outputStream = null;
            try {
                outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                printWriter.println(-1);
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            socket = null;

    }

    private static void login() {
        System.out.print("Enter your login:");
        String login = scanner.nextLine();
        System.out.print("Enter your password");
        String hashedpassword = scanner.nextLine();
        String password = hashingPassword(hashedpassword);
        try {
            RmiImpl rmiImpl = (RmiImpl) Naming.lookup("rmi://127.0.0.1:8078/login");
            loggedInUser = rmiImpl.login(login, password);

        } catch ( NotBoundException e ) {
            e.printStackTrace();
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        } catch ( RemoteException e ) {
            e.printStackTrace();
        }
        startSocket();


    }

    private static void startSocket() {
        try {
            socket = new Socket("127.0.0.1", 5050);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            printWriter.println(loggedInUser.getUserId());
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        listenToServer();
    }


    private static void register(){
        System.out.println("Enter your login: at least 5 charters && at most 19 charters");
        String login = scanner.nextLine();
        if(!(login.length()>4 && login.length()<20 )){
            System.out.println("Inavlid login you should have at least 5 charters and at most 19 charters");
            return;
        }
        System.out.println("Enter your first name:");
        String firstName = scanner.nextLine();
        System.out.println("Enter your last name:");
        String lastName = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String passwordToHash = scanner.nextLine();
        System.out.print("Repeat your password: ");
        String repeatPassword = scanner.nextLine();

        if (!passwordToHash.equals(repeatPassword)) {
            System.out.println("Invalid password");
            return;
        }
        String password = hashingPassword(passwordToHash);

        try {
            RmiImpl registerImpl = (RmiImpl) Naming.lookup("rmi://127.0.0.1:8078/register");
            registerImpl.register(login, password, firstName, lastName);

        } catch ( NotBoundException e ) {
            e.printStackTrace();
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
        } catch ( RemoteException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Using MD5 algorithm to encode the password
     * @param passwordToHash
     * @return hashed password
     */
    private static String hashingPassword(String passwordToHash) {

        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }
}

