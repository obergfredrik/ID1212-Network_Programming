/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201101
 *  Latest update: -
 *
 */


import java.io.*;
import java.net.Socket;

/**
 * Represents an instance of the chat client which connects to a chat server
 * where the user of the client can sen chat messages to other connected clients.
 */
public class ChatClient {

    private Socket clientSocket;
    private Thread sender;
    private Thread receiver;
    private String hostName = "localhost";
    private int portNumber = 1234;

    /**
     * Creates a sock from which a tcp session can be initiated. Two threads are also
     * created; one for sending messages and one for receiving.
     */
    private void createClient(){
        try{
            this.clientSocket = new Socket(this.hostName, this.portNumber);

            this.sender = new Thread(new MessageSender(clientSocket));
            this.receiver = new Thread(new MessageReceiver(clientSocket));
            sender.start();
            receiver.start();

        }catch (IOException e){
            System.out.println(e);
        }
    }

    /**
     * Called upon when the code is executed.
     * @param args is the entered parameters in the form of an array of strings.
     */
    public static void main( String[] args) {
        ChatClient client = new ChatClient();
        client.createClient();
    }
}


