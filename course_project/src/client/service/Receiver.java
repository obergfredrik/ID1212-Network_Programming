/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201217
 *  Latest update: -
 *
 */

package client.service;

import client.service.exceptions.ServerDisconnectedException;
import client.main.Client;
import javax.net.ssl.SSLSocket;
import java.io.*;

/**
 * Handles the receiving of messages and files in the chat client. Implements runnable so the class operates
 * in its own thread.
 */
public class Receiver implements Runnable{

    /**
     * Class attributes.
     */
    private final SSLSocket socket;
    private final Client client;
    private String message;
    private BufferedReader reader;

    /**
     * Creates an instance if the MessageReceiver class.
     *
     * @param socket is the socket from which messages are received.
     */
    public Receiver(SSLSocket socket, Client client){
        this.socket = socket;
        this.client = client;
    }

    /**
     * Is initiated when the threads start() function is called upon from the client object who instantiated t
     * his receive. Instantiates the class attribute reader handling the input from the user via standard in.
     * Thereafter calls the loggingIn and communicating methods representing those two states of the application.
     */
    @Override
    public void run() {

        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            loggingIn();
            communicating();

        }catch(IOException | ServerDisconnectedException e){
           e.printStackTrace();
           System.exit(1);
        }
    }

    /**
     * Called when the log in state is being initiated. Takes input from the server and states if the user has been logged in or
     * not based on the server message.
     *
     * @throws IOException if there has been some issues with the socket inputStream.
     */
    void loggingIn() throws IOException, ServerDisconnectedException {

        String[] split;

       do{
            message= reader.readLine();

            if (message == null)
                throw new ServerDisconnectedException("The server has disconnected");

            split = message.split(" ");

            if(split.length == 9)
                client.setLoggedIn(true);

            System.out.println(message);
        }while (!client.isLoggedIn());
    }


    /**
     * Called when the communication state of the application has been entered. Takes input the sockets inputStream received from
     * the server and processes it accordingly until the client is not logged in to the server chat application.
     *
     * @throws IOException if there has been some issues with the socket inputStream.
     */
    void communicating() throws IOException, ServerDisconnectedException {
        do{
            message = reader.readLine();

            if(message == null)
                throw new ServerDisconnectedException("The server has disconnected");

            System.out.println(message);

            if (client.isSending()) {
                sendingFile();
            }else if (client.isReceiving())
                receivingFile();


        } while (client.isLoggedIn());

    }

    /**
     * Called when the receiving state has been initiated from the associated <>Sender</> object. Takes the server message
     * and processes it and behaves accordingly and does so until the sending state is not indicated via the associated
     * <>Client</> sending attribute.
     *
     * @throws IOException if there has been some issues with the socket inputStream.
     */
    void sendingFile() throws IOException {

        String[] split;

        do {
            split = message.split(" ");

            if (split[0].equals("You"))
                client.setSending(false);
            else if(split[0].equals("Enter"))
                client.setServerTransferring(true);
            else {
                this.message = reader.readLine();
                System.out.println(message);
            }

        }while (client.isSending());
    }

    /**
     * Called when the receiving state has been initiated from the associated <>Sender</> object via the associated <>Client</> class attribute "receiving".
     *
     * @throws IOException if there has been some issues with the socket inputStream.
     */
    private void receivingFile() throws IOException {

        String[] split;

        do {

            split = message.split(" ", 2);

            if (split[0].equals("Enter")) {

                client.setServerTransferring(true);
                String[] fileContent = extractFileContent();

                if (fileContent[0].equals("file")) {
                    byte[] data = receiveFileData(Integer.parseInt(fileContent[2]));
                    storeFileToDisk(fileContent[1], data);
                } else
                    System.out.println(message);

            }else if(split[0].equals("You")){
                client.setReceiving(false);
            }else {
                this.message = reader.readLine();
                System.out.println(message);
            }


        }while (client.isReceiving());


    }

    /**
     * Stores the data received as a file on local disk storage.
     *
     * @param fileName is the name of the file being stored.
     * @param data is the data received from the server.
     * @throws IOException if there has been some issues with the file storing.
     */
    void storeFileToDisk(String fileName, byte[] data) throws IOException {

        String current = new File(".").getCanonicalPath();
        FileOutputStream fileOutputStream = new FileOutputStream(current + "/src/client/" + fileName);
        fileOutputStream.write(data);
        fileOutputStream.flush();
        System.out.println("File received correctly!");
    }

    /**
     * Receives file data from the server and stores it in a byte array.
     *
     * @param size is the size of the data in terms of bytes,
     * @return is the received data.
     * @throws IOException if there has been some issues with the socket inputStream.
     */
    byte[] receiveFileData(int size) throws IOException {

        InputStream inputStream = this.socket.getInputStream();
        byte[] data = new byte[size];
        inputStream.read(data, 0, data.length);

        return data;
    }

    /**
     * Extracts the name and size of the file data being received.
     *
     * @return is the received file content.
     * @throws IOException if there has been some issues with reading from the socket inputStream.
     */
    String[] extractFileContent() throws IOException {
        this.message = reader.readLine();
        return message.split(" ");
    }
}
