package am.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketServer {

    static Map<Integer,Socket> onlineUsers = new HashMap<>();



    void socketServer() {
        try{
            ServerSocket serverSocket = new ServerSocket(5050);
             while (true){
                 Socket socket = serverSocket.accept();
                 int clientId = getClientId(socket);
                 onlineUsers.put(clientId,socket);
                 System.out.println("connected");
                 new Thread(new ClientProcessor(socket,clientId)).start();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private int getClientId(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return Integer.parseInt(bufferedReader.readLine());
        }
        catch(IOException e){
            e.printStackTrace();

        }
        return -1;
    }

    public static boolean sendFriendRequest(int id, String firstName, String lastName, int frinedId) {
        Socket socket = onlineUsers.get(frinedId);
        if(socket==null)return false;
        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream,true);
            printWriter.println("$frf$ user by id: " + id + " " + firstName + " " + lastName + " want to be your friend");
            return true;
        }
        catch ( IOException e){
            e.printStackTrace();
            return false;
        }
    }
}

