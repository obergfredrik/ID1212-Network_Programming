/**
*  Created by: Fredrik Öberg
  *  Date of creation: 201101
  *  Latest update: -
  *
  */

package client.service;

import client.service.exceptions.FileTransferDeniedException;
import client.main.Client;
import javax.naming.SizeLimitExceededException;
import javax.net.ssl.SSLSocket;
import java.io.*;

/**
 * Handles the sending of messages and files in the chat client. Implements runnable so
 * the class instance operates on its own thread.
 */
public class Sender implements Runnable{

    /**
     * Class attributes.
     */
    private final SSLSocket socket;
    private BufferedReader reader;
    private PrintWriter sender;
    private String message;
    private final Client client;
    private final int MAX_FILE_SIZE = 1024*1024;
    private final int MAX_USERNAME_LENGTH = 32;
    private final int MAX_MESSAGE_LENGTH = 256;

    /**
     * Creates an instance of the Sender class.
     *
     * @param socket is the socket through which messages and files are sent.
     * @param client is the client object who created and initialized this class instance.
     */
    public Sender(SSLSocket socket, Client client){
        this.socket = socket;
        this.client = client;
    }

    /**
     * Is initiated when the threads start() function is called upon by the client object.
     * Creates and initializes the sender and reader class attributes and goes through the three
     * main phases of the chat session.
     */
    @Override
    public void run() {
        try {
            this.sender = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader((System.in)));

            loggingIn();
            communicating();
            ending();

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Represents the logIn state of the application. Takes user name input from the user and sends
     * it to the server and does this until the server has accepted the user name.
     *
     * @throws IOException when there has been some issues with the input from standard in.
     * @throws InterruptedException if the sleep process of the thread does not work as intended.
     */
   private void loggingIn() throws IOException, InterruptedException {
        do {
            message = this.reader.readLine();

            if (MAX_USERNAME_LENGTH <= message.length() || message.isEmpty())
                System.out.println("The user name can not be empty or longer than " + MAX_USERNAME_LENGTH + " characters. Choose another one!");
            else {
                this.sender.println(message);
                Thread.sleep(100);
            }

        }while (!client.isLoggedIn());
    }

    /**
     * Represents the communication state where the client sends messages and commands to the server. When
     * the commands -s, -g or -s are entered by the user the send, receive and end phases respectively
     * are being initiated.
     *
     * @throws IOException when there has been some issues with the input from standard in.
     * @throws InterruptedException if the sleep process of the thread does not work as intended.
     */
    private void communicating() throws IOException, InterruptedException {

        String[] split;

        do{
            message = this.reader.readLine();
            split = message.split(" ", 2);

            if(MAX_MESSAGE_LENGTH < message.length())
                System.out.println("The message is to long. Send a shorter one!");
            else if (split[0].equals("-n")){
                if (split.length > 1 && split[1].length() <= MAX_USERNAME_LENGTH)
                    this.sender.println(message);
                else
                    System.out.println("The user name can not be empty or longer than " + MAX_USERNAME_LENGTH + " characters. Choose another one!");
            }
            else if(split[0].equals("-s"))
                initiateSendFileState(split);
            else if(split[0].equals("-g"))
                initiateReceiveFileState(split);
            else
                this.sender.println(message);

        }while(!split[0].equals("-q"));

    }


    /**
     * Called when the receive state is being initiated. Checks if a name has been entered and, if true, sets the
     * receive attribute to true initiating the receive state and thereafter sends the complete file request to the server.
     */
    private void initiateReceiveFileState(String[] getRequest){

            if (1 == getRequest.length) {
                System.out.println("You need to enter a file name");
                return;
            }

            client.setReceiving(true);
            this.sender.println(message);
    }

    /**
     * Called when the sending state is being initiated. Sets the sending attribute to true and waits for
     * server to respond to the state initiation. Then takes the file name input from the user and tries
     * to locate the file and checks that the file is not to large.. If the file is located it sends it to
     * the server for remote storage.
     *
     * @throws IOException when there has been some issues with the input from standard in.
     */
    void initiateSendFileState(String[] input) throws IOException {

        String fileName = null;

        try {
            fileName = extractFileName(input[1]);
            File file = getFile(fileName);
            int size = getFileSize(file);
            FileInputStream fileInputStream = new FileInputStream(file);

            client.setSending(true);
            this.sender.println("-s " + fileName + " " + size);
            waitForServerSendResponse();
            sendFile(fileInputStream, size);

        } catch (FileNotFoundException e) {
           System. out.println("Could not find a file with the name \"" + fileName + "\"");
        } catch (FileTransferDeniedException e) {
            System.out.println("Enter a chat room to send a file to the server.");
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("You need to enter a file name.");
        } catch (SizeLimitExceededException e) {
            System.out.println("The size of the file is to large");
        }
    }

    /**
     * Extracts the file name from the input from standard in.
     *
     * @return is the name of the file of interest.
     */
    private String extractFileName(String input) {
       return input.split(" ", 2)[0];
    }

    /**
     * Waits for the server to send a response message to the send request and is received at the <>Receiver</> class. The receiver
     * sets the client attribute serverTransferring to true if the response is correct and the isSending
     * attribute to false if there has been some issues with the file transfer.
     *
     * @throws FileTransferDeniedException is thrown when the server denies the file transfer.
     */
    private void waitForServerSendResponse() throws FileTransferDeniedException {

        while (!client.isServerTransferring())
            if (!client.isSending())
                throw new FileTransferDeniedException("The server denied the file transfer initiation");

    }

    /**
     * Tries to get the file from local storage.
     *
     * @param fileName is the name of the file of interest.
     * @return is the located file.
     * @throws IOException when the file could not be located.
     */
    private File getFile(String fileName) throws IOException {

        String current = new java.io.File(".").getCanonicalPath();
        return  new File(current + "/" + fileName);
    }

    /**
     * Extracts a file and stores it in an array of bytes and sends the byte array to the server.
     *
     * @param fileInputStream is the object retrieving the file from local storage and stores in an array of byte.
     * @param size is the size of the file being sent
     * @throws IOException when there has been some issues with the reading of the file.
     */
    private void sendFile(FileInputStream fileInputStream,  int size ) throws IOException {

        byte[] data = new byte[size];
        OutputStream outputStream = socket.getOutputStream();
        fileInputStream.read(data);
        outputStream.write(data,0,data.length);
        outputStream.flush();
    }

    /**
     * Extract the size of a given file and checks that the file is not to large.
     *
     * @param file is the file whose size if of importance.
     * @return is the size of the file.
     */
    private int getFileSize(File file ) throws SizeLimitExceededException {

        int size = (int) file.length();

        if (MAX_FILE_SIZE < size)
           throw new SizeLimitExceededException();

        return size;
    }

    /**
     * Called when the user has sent a -q command to the server thereby ending the connection.
     *
     * @throws InterruptedException is there has been som issues with the thread sleeping.
     * @throws IOException if the closing of the socket attribute does not work as intended.
     */
    private void ending() throws InterruptedException, IOException {

        this.client.setLoggedIn(false);
        Thread.sleep(500);
        this.socket.close();

    }

}
