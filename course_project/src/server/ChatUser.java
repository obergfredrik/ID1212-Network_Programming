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
public class ChatUser implements Runnable {

    private final SSLSocket socket;
    private ChatRoom chatRoom;
    private String userName;
    private PrintWriter sender;
    private BufferedReader receiver;

    /**
     * Creates a ChatMember object
     *
     * @param socket is the socket of the ChatServer object.
     */
    ChatUser(SSLSocket socket) {
        this.socket = socket;

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
        try {


            this.chatRoom.distributeMessage(userName + " has entered " + this.chatRoom.getChatRoomName() + " !");

            String message;

            while (true) {
                message = receiveMessage();

                if(message.equals("Bye!")){
                    this.chatRoom.removeChatUser(this);
                    break;
                }else
                    this.chatRoom.distributeMessage("[" + this.userName + "] " + message);
            }

            this.socket.close();

        }catch (IllegalArgumentException | IOException e){
            System.out.println(e);
        }
    }

    /**
     * Is called upon when a new message has been entered by the connected client.
     * @param message is the message being sent to the chat client.
     */
    void sendMessage(String message){
        this.sender.println(message);
    }

    String receiveMessage() throws IOException {
        return this.receiver.readLine();
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