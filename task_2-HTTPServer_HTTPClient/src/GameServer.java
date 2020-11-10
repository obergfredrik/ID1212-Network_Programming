/**
 * Author: Fredrik Öberg
 * Date of Creation: 201110
 * Date of Latest Update: -
 *
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents a server which Web-Browser can connect to and communicate via HTTP.
 *
 */
public class GameServer {

    private ServerSocket serverSocket;
    private int portNumber = 1234;

    /**
     * Initiates the server with the given port number.
     */
    private void initiateServer(){
        try {
            this.serverSocket = new ServerSocket(portNumber);
            System.out.println("The server has been created");

            while (true){
                Socket socket = this.serverSocket.accept();
                GameSession session = new GameSession(socket);
                Thread thread = new Thread(session);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called upon when the program is executed.
     * @param args is a set of arguments in the form of an array of strings sent via the command line.
     */
    public static void main(String[] args){

        GameServer server = new GameServer();
        server.initiateServer();

    }
}
