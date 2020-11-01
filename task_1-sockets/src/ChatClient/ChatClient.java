package ChatClient;

import java.io.*;
import java.net.Socket;

public class ChatClient {

    private static Socket clientSocket;
    private String hostName = "localhost";
    private int portNumber = 1234;

    private void createClient(){

        try{
            this.clientSocket = new Socket(this.hostName, this.portNumber);

            Thread sender = new Thread(new MessageSender(clientSocket));
            Thread receiver = new Thread(new MessageReceiver(clientSocket));
            sender.start();
            receiver.start();

        }catch (IOException e){
        }
    }

    public static void main( String[] args) {


        ChatClient client = new ChatClient();
        client.createClient();

    }
}


