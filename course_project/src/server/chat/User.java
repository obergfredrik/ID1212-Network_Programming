package server.chat;

/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201101
 *  Latest update: -
 *
 */

import server.model.ChatFile;
import server.service.Messenger;

import javax.net.ssl.SSLSocket;
import java.io.*;

/**
 * Represents a member of the chat server. Implements runnable and
 * therefore has its own associated thread.
 */
public class User implements Runnable {

    private final SSLSocket socket;
    private final Messenger messenger;
    private Room room;
    private String userName;
    private PrintWriter sender;
    private BufferedReader receiver;
    private boolean loggedIn;
    private boolean transferring;

    /**
     * Creates a ChatMember object
     *
     * @param socket is the socket of the ChatServer object.
     */
    User(SSLSocket socket, Messenger messenger) {
        this.socket = socket;
        this.messenger = messenger;
        this.loggedIn = false;
        this.userName = "";
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
            this.receiver = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.sender = new PrintWriter(this.socket.getOutputStream(), true);

            loggingIn();
            communicating();
            ending();

        }catch (IllegalArgumentException | IOException e){
            System.out.println(e);
        }
    }

    void loggingIn() throws IOException {

        String userName;

        messenger.connectRequest(this);

        do {
            userName = receiveMessage();
            String[] split = userName.split(" ", 2);
            messenger.loginRequest(this, split[0]);
        }while (!isLoggedIn());
    }

    void communicating() throws IOException {

        String message;

        do{
            message = receiveMessage();
            this.messenger.handleMessage(message, this);
        }while (this.loggedIn);
    }


    private String receiveMessage() throws IOException {
        return this.receiver.readLine();
    }

    public boolean inChatRoom(){
        return room != null;
    }



    /**
     * Is called upon when a new message has been entered by the connected client.
     * @param message is the message being sent to the chat client.
     */
    public void sendMessage(String message){
        this.sender.println(message);
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
    /**
     * Returns the user name of the connected client.
     * @return is the connected clients user name.
     */
    public String getUserName(){
        return this.userName;
    }

    public Room getChatRoom() {
        return room;
    }

    public void setChatRoom(Room room) {
        this.room = room;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isTransferring() {
        return transferring;
    }

    public void setTransferring(boolean transferring) {
        this.transferring = transferring;
    }


    public void receiveFileFromClient(String fileName, int size) throws IOException {

        InputStream inputStream = this.socket.getInputStream();
        byte[] data = new byte[size];
        inputStream.read(data, 0, data.length);
        ChatFile file = new ChatFile(fileName, data);
        getChatRoom().addChatFile(file);
        sendMessage("File uploaded correctly");
    }

    public void sendFileToClient(ChatFile chatFile) throws IOException {

        byte[] data = chatFile.getData();

        sendMessage("file " + chatFile.getName() + " " + data.length);

        OutputStream outputStream = socket.getOutputStream();

        outputStream.write(data,0,data.length);
        outputStream.flush();
    }

    void ending() throws IOException {
        this.socket.close();
    }
}