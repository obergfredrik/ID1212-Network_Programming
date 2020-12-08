package server;

/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201101
 *  Latest update: -
 *
 */

import javax.net.ssl.SSLSocket;
import java.io.*;

/**
 * Represents a member of the chat server. Implements runnable and
 * therefore has its own associated thread.
 */
public class User implements Runnable {

    private final SSLSocket socket;
    private ChatRoom chatRoom;
    private String userName;
    private PrintWriter sender;
    private BufferedReader receiver;
    private boolean connected;
    private MessageHandler messageHandler;

    /**
     * Creates a ChatMember object
     *
     * @param socket is the socket of the ChatServer object.
     */
    User(SSLSocket socket, MessageHandler messageHandler) {
        this.socket = socket;
        this.messageHandler = messageHandler;
        this.connected = true;
        this.userName = "anonymous";

        try{
            InputStream input = this.socket.getInputStream();
            this.receiver = new BufferedReader(new InputStreamReader(input));
            this.sender = new PrintWriter(this.socket.getOutputStream(), true);

        }catch (IOException e){
            System.out.println(e);
        }
    }

    /**
     * Is called upon from a threads start method when a ChatServer has
     * created a new ChatMember object. I prompts for the user connecting to
     * the chat server to enter a user name and distributes the following
     * messages entered by the user until the command quit has been entered,
     * thereby closing the connection and removing the user from the server.
     */
    public void run() {
        String message;

        sendMessage("Welcome to the chat! Please enter your user name: ");

        try {

            createUserName();
            sendMessage("Hi " + getUserName() + "! You are currently in the chat lobby.\nType -help if you need help to get started");

            do{
             message = this.receiver.readLine();
             this.messageHandler.handleMessage(message, this);
            }while (this.connected);

            this.socket.close();
        }catch (IllegalArgumentException | IOException e){
            System.out.println(e);
        }
    }


    void createUserName() throws IOException {

        boolean userNameCreated;

        do {
           userNameCreated = this.messageHandler.updateUserName(this, this.receiver.readLine());
        }while (!userNameCreated);

    }

    void leaveChatRoom(){
        this.chatRoom.removeUser(this);
        setChatRoom(null);
    }

    void deConnect(){
        this.connected = false;
    }

    /**
     * Is called upon when a new message has been entered by the connected client.
     * @param message is the message being sent to the chat client.
     */
    void sendMessage(String message){
        this.sender.println(message);
    }

    void setUserName(String userName){
        this.userName = userName;
    }
    /**
     * Returns the user name of the connected client.
     * @return is the connected clients user name.
     */
    String getUserName(){
        return this.userName;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}