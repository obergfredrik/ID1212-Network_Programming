package ChatClient;

import java.io.*;
import java.net.Socket;

public class MessageSender implements Runnable{

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter sender;
    private String message;

    public MessageSender(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.sender = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader((System.in)));

            while(true) {
                message = this.reader.readLine();
                sender.println(message);
            }
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
