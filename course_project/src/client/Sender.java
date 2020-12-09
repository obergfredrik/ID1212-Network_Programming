package client; /**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201101
 *  Latest update: -
 *
 */

import javax.net.ssl.SSLSocket;
import java.io.*;


/**
 * Handles the sending of messages in the chat client. Implements runnable so
 * the class operates in its own thread.
 */
public class Sender implements Runnable{

    private SSLSocket socket;
    private BufferedReader reader;
    private PrintWriter sender;
    private String message;

    /**
     * Creates an instance of the MessageSender class.
     * @param socket is the socket through which messages are sent.
     */
    public Sender(SSLSocket socket){
        this.socket = socket;
    }

    /**
     * Is initiated when the threads start() function is called upon. Handles the output from, the
     * chat client. Takes messages from standard in and sends it to the connected chat server. If the message
     * "quit" is entered the client exits the chat session by closing the connected socket.
     */
    @Override
    public void run() {
        try {
            this.sender = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader((System.in)));

            do{
                this.message = this.reader.readLine();
                this.sender.println(message);
            }while(!this.message.equals("-quit"));

            Thread.sleep(500);
            this.socket.close();


        } catch (IOException | InterruptedException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
