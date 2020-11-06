import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {

    private ServerSocket serverSocket;
    private int portNumber = 1234;
    private List<GameSession> sessions = new ArrayList();
    private List<Thread> threads = new ArrayList<>();

    private void initiateServer(){
        try {
            this.serverSocket = new ServerSocket(portNumber);
            System.out.println("The server has been created");

            while (true){
                Socket socket = this.serverSocket.accept();
                GameSession session = new GameSession(socket);
                Thread thread = new Thread(session);
                thread.start();
                this.sessions.add(session);
                this.threads.add(thread);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){

        GameServer server = new GameServer();
        server.initiateServer();

    }
}
