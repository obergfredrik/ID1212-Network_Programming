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
    private Client client;
    private String message;
    private BufferedReader reader;

    /**
     * Creates an instance if the MessageReceiver class.
     * @param socket is the socket from which messages are received.
     */
    Receiver(SSLSocket socket, Client client){
        this.socket = socket;
        this.client = client;
    }

    /**
     * Is initiated when the threads start() function is called upon. Handles the input from, the chat client.
     */
    @Override
    public void run() {

        try {
            InputStream input = socket.getInputStream();
             this.reader = new BufferedReader(new InputStreamReader(input));

            loggingIn();

            while (true) {

                if(message == null)
                    break;
                else if (client.isSending()) {
                    sendingFile();
                }else if (client.isReceiving()) {


                    receivingFile();
                }else {
                    this.message = reader.readLine();
                    System.out.println(this.message);
                }
            }

        }catch (IllegalArgumentException | IOException e){
            System.out.println(e);
        }
    }

    private void receivingFile() throws IOException {

        String[] split = message.split(" ", 2);
        if(split[0].equals("Enter")){
            client.setServerIsSending(true);
            this.message = reader.readLine();
            String[] fileContent = message.split(" ");
            if (fileContent[0].equals("file") ) {
                InputStream inputStream = this.socket.getInputStream();
                String fileName = fileContent[1];
                int size = Integer.parseInt(fileContent[2]);
                byte[] data = new byte[size];
                inputStream.read(data, 0, data.length);
                String current = new java.io.File(".").getCanonicalPath();
                FileOutputStream fileOutputStream = new FileOutputStream(current + "/src/client/" + fileName);
                fileOutputStream.write(data);
                fileOutputStream.flush();
                System.out.println("File received correctly!");
            }else
                System.out.println(message);

        }else
            System.out.println("There was an error with the file transfer. Please try again.");


        client.setReceiving(false);
    }

    void sendingFile() throws IOException {


        String[] split = message.split(" ");

        if (split.length == 11)
            client.setSending(false);
        else
           client.setServerIsReceiving(true);


    }

    void loggingIn() throws IOException {

        String[] split;

        while (!client.isLoggedIn()){

            this.message = reader.readLine();

            split = this.message.split(" ");

            if(split.length == 9)
                client.setLoggedIn(true);

            System.out.println(this.message);
        }
    }
}
