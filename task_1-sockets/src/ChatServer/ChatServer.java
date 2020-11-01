package ChatServer;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private ServerSocket serverSocket;
    private Socket socket;
    private List<ChatMember> chatMembers;
    private int portNumber;

    private void createServer(int portNumber) {

        this.chatMembers = new ArrayList<>();

        try {
            this.serverSocket = new ServerSocket(portNumber);

            do {
                this.socket = this.serverSocket.accept();
                System.out.println("The server is created");
                ChatMember member = new ChatMember(socket, this);
                Thread user = new Thread(member);
                user.start();
                chatMembers.add(member);
            }while(true);



        } catch (IOException e) {
            System.out.println("Could not create server");
        }
    }

    void distributeMessage(String message){
        for (ChatMember member: this.chatMembers)
            member.sendMessage(message);

    }

    public static void main(String[] args) {
        try {
            int portNumber;

            if (0 < args.length) {
                portNumber = Integer.parseInt(args[0]);

                if (0 > portNumber || 65535 < portNumber)
                    throw new NumberFormatException();
            } else
                portNumber = 1234;

            ChatServer chatServer = new ChatServer();
            chatServer.createServer(portNumber);

        } catch (NumberFormatException e) {
            System.out.println("The value entered was not an integer between 0 and 65535");
        }
    }
}
