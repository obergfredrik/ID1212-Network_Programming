package ChatServer;

import java.net.*;
import java.io.*;

/**
 *
 */
public class ChatMember implements Runnable {

    private Socket socket;
    private String userName;
    private ChatServer server;
    private PrintWriter sender;
    private BufferedReader reader;
    /**
     * Creates a ChatMember object
     *
     * @param socket is the socket of the ChatServer object.
     */
    ChatMember(Socket socket, ChatServer server) throws SocketException {
        this.socket = socket;
        this.server = server;

        try{
            InputStream input = this.socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(input));
            this.sender = new PrintWriter(this.socket.getOutputStream(), true);

        }catch (IOException e){
        }
    }

    /**
     * Is called upon from a threads start method when a connection has been made between the socket and a client.
     */
    public void run() {
        try {
            this.sender.println("Hello! Please enter your user name: ");

            userName = this.reader.readLine();

            this.server.distributeMessage(userName + " has entered the chat!");

            String message;

            while (true) {
                message = this.reader.readLine();
                this.server.distributeMessage("[" + this.userName + "] " + message);
            }
        }catch (IllegalArgumentException | IOException e){

        }
    }

    void sendMessage(String message){
        this.sender.println(message);
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName(){
        return this.userName;
    }

}