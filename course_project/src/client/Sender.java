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
    private Client client;
    private int MAX_FILE_SIZE = 1024*1024;
    /**
     * Creates an instance of the MessageSender class.
     * @param socket is the socket through which messages are sent.
     */
    public Sender(SSLSocket socket, Client client){
        this.socket = socket;
        this.client = client;
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

            loggingIn();

            this.sender.println("-j a");

            do{
                this.message = this.reader.readLine();

                this.sender.println(message);


                if(message.equals("-s"))
                    sendFile();
                else if(message.equals("-g"))
                    receiveFile();

            }while(!this.message.equals("-q"));

            Thread.sleep(500);
            this.socket.close();


        } catch (IOException | InterruptedException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void receiveFile() throws IOException {
        client.setReceiving(true);

        while (!client.isServerIsSending())
            if (!client.isReceiving())
                return;

        String fileName = this.reader.readLine();
        this.sender.println("-g " + fileName);
    }

    void loggingIn() throws IOException, InterruptedException {

        do {

           // this.message = this.reader.readLine();
            this.sender.println("asdqwe");

            Thread.sleep(100);

        }while (!client.isLoggedIn());
    }

    void sendFile() throws IOException, InterruptedException {

        client.setSending(true);

        while (!client.isServerIsReceiving())
            if (!client.isSending())
                return;

        String fileName = this.reader.readLine();

        try {
            String current = new java.io.File(".").getCanonicalPath();
            File file = new File(current + "/" + fileName);

            int size = (int)file.length();

            if (MAX_FILE_SIZE < size) {
                this.sender.println("-s size");
                return;
            }

            this.sender.println("-s " + fileName + " " + size);

            byte[] data = new byte[(int)file.length()];

            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = socket.getOutputStream();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.read(data,0,data.length);


            outputStream.write(data,0,data.length);
            outputStream.flush();

        }catch (FileNotFoundException e){
            this.sender.println("-s file " + fileName);
        }

        client.setSending(false);
    }

}
