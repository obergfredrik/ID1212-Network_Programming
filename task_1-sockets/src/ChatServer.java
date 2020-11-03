/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201101
 *  Latest update: -
 *
 */

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chat server which users can connect to and send messages between other connected users.
 *
 */
public class ChatServer {

    private ServerSocket serverSocket;
    private Socket socket;
    private List<ChatMember> chatMembers;
    private List<Thread> threads;

    /**
     * Is called upon when a chat server is being created.
     * @param portNumber is the port number of the created chat server.
     */
    private void createServer(int portNumber) {

        this.chatMembers = new ArrayList<>();
        this.threads = new ArrayList<>();

        try {
            this.serverSocket = new ServerSocket(portNumber);
            System.out.println("The server has been created");

            do {
                this.socket = this.serverSocket.accept();
                ChatMember member = new ChatMember(socket, this);
                Thread thread = new Thread(member);
                thread.start();
                this.chatMembers.add(member);
                this.threads.add(thread);
                System.out.println("Number of connected users: " + this.chatMembers.size());
            }while(true);

        } catch (IOException e) {
            System.out.println("Could not create server");
        }
    }

    /**
     * Distributes the entered chat messages between all other connected users.
     * @param message is the message being distributed.
     */
    void distributeMessage(String message){
        for (ChatMember member: this.chatMembers)
            member.sendMessage(message);
    }

    /**
     * Removes a user and its associated thread from the chatMembers and threads lists.
     * @param member is the chat member being removed
     */
    void removeUser(ChatMember member){
        for (int i = 0; i < this.chatMembers.size(); i++){
            if(this.chatMembers.get(i).equals(member)){
                this.chatMembers.remove(i);
                this.threads.remove(i);
            }
        }
        distributeMessage(member.getUserName() + " has left the chat!");
        System.out.println("Number of connected users: " + this.chatMembers.size());
    }

    /**
     * Callud upon when the program is executed.
     * @param args contains the arguments sent via the command line.
     */
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
