package client; /**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201101
 *  Latest update: -
 *
 */


import javax.net.ssl.SSLSocket;
import java.io.*;


/**
 * Handles the receiving of messages in the chat client. Implements runnable so t
 * he class operates in its own thread.
 */
 class Receiver implements Runnable{

    private SSLSocket socket;

    /**
     * Creates an instance if the MessageReceiver class.
     * @param socket is the socket from which messages are received.
     */
    Receiver(SSLSocket socket){
        this.socket = socket;
    }

    /**
     * Is initiated when the threads start() function is called upon. Handles the input from, the chat client.
     */
    @Override
    public void run() {

        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String message;

            while (true) {
                message = reader.readLine();

                if(null == message )
                    break;
                else
                    System.out.println(message);
            }

        }catch (IllegalArgumentException | IOException e){
            System.out.println(e);
        }
    }
}
