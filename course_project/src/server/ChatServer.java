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

/**
 * Represents a chat server which users can connect to and send messages between other connected users.
 *
 */
public class ChatServer {

    private final SSLServerSocketFactory socketFactory;
    private ChatHandler chatHandler;


    ChatServer() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, CertificateException, NoSuchProviderException, IOException {

        char[] password = "rootroot".toCharArray();
        this.chatHandler = new ChatHandler();

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
    private void initiateServer(int portNumber) {

        try {
            SSLServerSocket serverSocket = (SSLServerSocket) this.socketFactory.createServerSocket(portNumber);
            System.out.println("The server has been created");

            SSLSocket socket;

            do {
                socket = (SSLSocket) serverSocket.accept();
                this.chatHandler.newUser(socket);

            }while(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called upon when the program is executed.
     *
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

        chatServer.initiateServer(portNumber);
    }
}
