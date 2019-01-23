package am.chat;

import java.io.*;
import java.net.Socket;



public class ClientProcessor implements Runnable {
    Socket socket;
    int userid;
    boolean readed;

    public ClientProcessor(Socket socket, int ClientId) {
        this.userid=ClientId;
        this.socket = socket;
    }


    @Override
    public void run() {
        while(true) {

            InputStream inputStream = null;
            OutputStream outputStream = null;
            readed=false;

            try {
                inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                int recivedUserId = Integer.parseInt(bufferedReader.readLine());

                if(recivedUserId<0){
                    closeConnection(outputStream,"%1close1%");
                    break;
                }


                String recivedMessage = bufferedReader.readLine();
                Socket socketToClient = SocketServer.onlineUsers.get(recivedUserId);

                if(!(Server.checkfriendship(userid,recivedUserId))){
                    checkfriend(outputStream, "you can't write to this user because you are not friends");
                    continue;
                }
                if(socketToClient != null){
                    outputStream = socketToClient.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    printWriter.println(recivedMessage);
                    readed= true;
                }

                Server.sendMessageToDB(userid,recivedUserId,recivedMessage,readed);

            } catch (Exception e ) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void checkfriend(OutputStream outputStream, String message) {
        try {
            outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            printWriter.println(message);
        }
        catch ( IOException e ){
            e.printStackTrace();
        }
    }

    private void closeConnection(OutputStream outputStream, String message) {
        try {
            outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            printWriter.println(message);
            SocketServer.onlineUsers.remove(userid);
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

}
