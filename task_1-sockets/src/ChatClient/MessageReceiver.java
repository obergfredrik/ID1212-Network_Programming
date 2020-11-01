package ChatClient;

import java.io.*;
import java.net.Socket;

public class MessageReceiver implements Runnable{

    private Socket socket;

    MessageReceiver(Socket socket){

        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String message;

            while (true) {
                message = reader.readLine();
                System.out.println(message);
            }

        }catch (IllegalArgumentException | IOException e){
        }
    }
}
