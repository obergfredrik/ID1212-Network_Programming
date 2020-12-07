package server; /**
 *  Created by: Fredrik Öberg
 *  Date of creation:
 *  Latest update: -
 *
 */

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chat server which users can connect to and send messages between other connected users.
 *
 */
public class ChatServer {

    private final SSLServerSocketFactory socketFactory;
    private List<ChatRoom> chatRooms;
    private List<ChatMember> chatMembers;
    private List<Thread> memberThreads;

    ChatServer() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, CertificateException, NoSuchProviderException, IOException {

        char[] password = "rootroot".toCharArray();

        KeyStore keyStore = createKeyStore(password);
        SSLContext context = createContext(keyStore, password);
        this.socketFactory = context.getServerSocketFactory();
    }

    KeyStore createKeyStore(char[] passWord) throws IOException, NoSuchProviderException, KeyStoreException, CertificateException, NoSuchAlgorithmException {

        InputStream inputStream = new FileInputStream(new File("src/server/.keystore"));
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
        keyStore.load(inputStream, passWord);

        return keyStore;
    }

    SSLContext createContext(KeyStore keyStore, char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, KeyManagementException {

        SSLContext context = SSLContext.getInstance("TLS");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        context.init(keyManagerFactory.getKeyManagers(), null, null);

        return context;
    }

    /**
     * Is called upon when a chat server is being created.
     * @param portNumber is the port number of the created chat server.
     */
    private void createServer(int portNumber) {

        this.chatMembers = new ArrayList<>();
        this.memberThreads = new ArrayList<>();

        try {
            SSLServerSocket serverSocket = (SSLServerSocket) this.socketFactory.createServerSocket(portNumber);
            System.out.println("The server has been created");

            SSLSocket socket;
            do {
                socket = (SSLSocket) serverSocket.accept();
                ChatMember member = new ChatMember(socket, this);
                Thread thread = new Thread(member);
                thread.start();
                this.chatMembers.add(member);
                this.memberThreads.add(thread);
                System.out.println("Number of connected users: " + this.chatMembers.size());
            }while(true);

        } catch (IOException e) {
            e.printStackTrace();
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
                this.memberThreads.remove(i);
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

            int portNumber;

            if (0 < args.length) {
                portNumber = Integer.parseInt(args[0]);

                if (0 > portNumber || 65535 < portNumber)
                    throw new NumberFormatException();
            } else
                portNumber = 1234;

        ChatServer chatServer = null;

        try {
            chatServer = new ChatServer();
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException | NoSuchProviderException | IOException e) {
            e.printStackTrace();
        }

        chatServer.createServer(portNumber);
    }
}
