/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201218
 *  Latest update: -
 *
 */


package server.chat;

import server.service.Messenger;
import javax.net.ssl.SSLSocket;
import java.io.*;

/**
 * Represents a user of the chat server and build up mainly of three stages loggingIn, communication and ending with the
 * side phases sending and receiving files. Messages as well as files sent to and from the client are received in this class.
 * The class implements runnable and therefore has its own associated thread.
 */
 public class User implements Runnable {

    /**
     * Class attributes.
     */
    private final SSLSocket socket;
    private final Messenger messenger;
    private boolean loggedIn;
    private Room room;
    private String userName;
    private PrintWriter sender;
    private BufferedReader receiver;

    /**
     * A constructor setting some of the class attributes.
     *
     * @param socket is the socke through which communication with the client is enabled.
     * @param messenger is the object processing the received and sent content.
     */
    User(SSLSocket socket, Messenger messenger) {
        this.socket = socket;
        this.messenger = messenger;
        this.loggedIn = false;
        this.userName = "";
    }

    /**
     * Is called upon from a threads start method when a server has
     * created a new user object. Ii first instantiates the class attributes sender and
     * receiver followed by prompts for the user connecting to the chat server to enter
     * a user name through the method call loggingIn(). The communication method is
     * the main phase of the user attribute handling most of the communication with
     * tha client and lastly is ending method call when a client has terminated the communication.
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
        }catch (NullPointerException e){
            System.out.println(e);
            this.messenger.handleQuitRequest(this);
        }
    }

    /**
     * Prompts a user to enter a user name and checks if it is valid by calling the method loginRequest
     * in the messenger object.
     *
     * @throws IOException if there has been som issues with the receiveMessage method call.
     */
   private void loggingIn() throws IOException {

        String userName;

        messenger.handleConnection(this);

        do {
            userName = receiveMessage();


            String[] split = userName.split(" ", 2);
            messenger.handleLogIn(this, split[0]);
        }while (!isLoggedIn());
    }

    /**
     * Receives messages sent from the client and sends it tk the messenger method handleMessage for processing.
     *
     * @throws IOException if there has been som issues with the receiveMessage method call.
     */
    private void communicating() throws IOException {

        String message;

        do{
            message = receiveMessage();
            this.messenger.handleCommunication(message, this);
        }while (this.loggedIn);
    }

    /**
     * Closes the socket class attribute,
     *
     * @throws IOException if there has been some issues with the closing of the socket.
     */
    private void ending() throws IOException {
        this.socket.close();
    }

    /**
     * Receives a file sent from the chat client,
     *
     * @param fileName is the name of the file.
     * @param size is the size of the file.
     * @throws IOException if there has been some issues with the inputStream.
     */
    public void receiveFileFromClient(String fileName, int size) throws IOException {

        InputStream inputStream = this.socket.getInputStream();
        byte[] data = new byte[size];
        inputStream.read(data, 0, data.length);
        ChatFile file = new ChatFile(fileName, data);
        getChatRoom().addChatFile(file);
    }

    /**
     * Sends a file to the connected client.
     *
     * @param data is the files data in teh form of an rray of bytes.
     * @throws IOException if there has been some issues with the outPutStream
     */
    public void sendFileToClient( byte[] data) throws IOException {

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(data,0,data.length);
        outputStream.flush();
    }

    /**
     * Sends a message to the connected client.
     *
     * @param message is the message being sent to the chat client.
     */
    public void sendMessage(String message){
        this.sender.println(message);
    }

    /**
     * Receives a message sent from the users client.
     *
     * @return is the message received.
     * @throws IOException if there has been som issues with the socket inputStream.
     */
    private String receiveMessage() throws IOException {
        return this.receiver.readLine();
    }

    /**
     * States if the user is in a chat room.
     *
     * @return is true if the user is in a chat room.
     */
    public boolean inChatRoom(){
        return room != null;
    }


    /**
     * A setter for the class attribute userName.
     *
     * @param userName is the new value of teh class attribute userName.
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * Returns the user name of the connected client.
     *
     * @return is the connected clients user name.
     */
    public String getUserName(){
        return this.userName;
    }

    /**
     * A getter for the room class attribute.
     *
     * @return is the value of the room class attribute,
     */
    public Room getChatRoom() {
        return room;
    }

    /**
     * A setter for the class attribute room.
     *
     * @param room is the new value of teh class attribute room.
     */
    public void setChatRoom(Room room) {
        this.room = room;
    }

    /**
     * States if teh user is logged in or not.
     *
     * @return is true if the user is logged in.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

     /**
     * A setter for the class attribute loggedIn.
     *
     * @param loggedIn is the new value of teh class attribute logedIn.
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }


}