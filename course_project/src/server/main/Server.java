 /*
 *  Created by: Fredrik Öberg
 *  Date of creation: 201217
 *  Latest update: -
 *
 */

 package server.main;

import server.chat.Lobby;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Represents a chat server which users can connect to and send messages files between other connected users.
 *
 */
public class Server {

    /**
     * Class attributes.
     */
    private final SSLServerSocketFactory socketFactory;
    private final Lobby lobby;

    /**
     * A constructor.
     *
     * @throws UnrecoverableKeyException when a keyManagerFactory object could not be created.
     * @throws NoSuchAlgorithmException when either a keystore, keyManagerFactory or SSLContext object could not be created.
     * @throws KeyStoreException when either a keystore or keyManagerFactory object could not be created.
     * @throws KeyManagementException when a SSLContext object could not be created.
     * @throws CertificateException when a KeyStore object could not be created.
     * @throws NoSuchProviderException when a KeyStore object could not be created.
     * @throws IOException when a KeyStore object could not be created.
     */
    private Server() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, CertificateException, NoSuchProviderException, IOException {

        char[] password = "rootroot".toCharArray();
        this.lobby = new Lobby();

        KeyStore keyStore = createKeyStore(password);
        KeyManagerFactory keyManagerFactory = createKeyManagerFactory(keyStore, password);
        SSLContext context = createContext(keyManagerFactory);
        this.socketFactory = context.getServerSocketFactory();
    }

    /**
     * Creates a KeyStore object.
     *
     * @param passWord is the password for the .keystore file stored on computer disk.
     * @return is the created keystore.
     * @throws IOException if the .keystore file could not be loaded from disk
     * @throws NoSuchProviderException if the chosen protocols do not match the existing ones.
     * @throws KeyStoreException if no Provider supports a KeyStoreSpi implementation for the specified type.
     * @throws CertificateException if any of the certificates in the keystore could not be loaded.
     * @throws NoSuchAlgorithmException  if the algorithm used to check the integrity of the keystore cannot be found.
     */
    private KeyStore createKeyStore(char[] passWord) throws IOException, NoSuchProviderException, KeyStoreException, CertificateException, NoSuchAlgorithmException {

        InputStream inputStream = new FileInputStream("src/server/.keystore");
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
        keyStore.load(inputStream, passWord);

        return keyStore;
    }

    /**
     * Creates a KeyManagerFactory object.
     *
     * @param keyStore is the keystore used when initializing the KeyManagerFactory.
     * @param password  is the password used when initializing the KeyManagerFactory.
     * @return is the created KeyManagerFactory.
     * @throws NoSuchAlgorithmException if no Provider supports a KeyManagerFactorySpi implementation for the specified algorithm.
     * @throws UnrecoverableKeyException if the key cannot be recovered (e.g. the given password is wrong).
     * @throws KeyStoreException if this operation fails.
     */
    private KeyManagerFactory createKeyManagerFactory(KeyStore keyStore, char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);

        return keyManagerFactory;
    }

    /**
     * Creates a SSLContext object.
     *
     * @param keyManagerFactory is used when creating the SSLContext.
     * @return is the created SSLContext.
     * @throws NoSuchAlgorithmException if no Provider supports a SSLContextSpi implementation for the specified protocol.
     * @throws KeyManagementException if the init operation fails.
     */
    private SSLContext createContext(KeyManagerFactory keyManagerFactory) throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, null);

        return context;
    }

    /**
     * Is called upon when a chat server is being initiated.
     *
     * @param portNumber is the port number of the created chat server.
     */
    private void initiateServer(int portNumber) {

        try {
            SSLServerSocket serverSocket = (SSLServerSocket) this.socketFactory.createServerSocket(portNumber);
            System.out.println("The server has been created");

            SSLSocket socket;

            do {
                socket = (SSLSocket) serverSocket.accept();
                this.lobby.newUser(socket);
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

        Server server;

        try {
            server = new Server();
            server.initiateServer(portNumber);
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException | NoSuchProviderException | IOException e) {
            e.printStackTrace();
        }

    }
}
