import java.io.*;
import java.net.Socket;

public class GameSession implements Runnable{

    private Socket socket;
    private BufferedReader receiver;
    private PrintWriter sender;

    public GameSession(Socket socket) {
        this.socket = socket;

        try {
            InputStream inputStream = socket.getInputStream();
            this.receiver = new BufferedReader(new InputStreamReader(inputStream));
            this.sender = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

            HTTPResponse response = new HTTPResponse();

            try {
                System.out.println(this.receiver.readLine());


                response.HTTPOk(this.sender,"Welcome to the number guess game.");

            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}
