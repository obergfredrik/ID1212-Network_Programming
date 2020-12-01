/**
 * Author: Fredrik Öberg
 * Date of Creation: 201110
 * Date of Latest Update: 201201
 *
 */

package extra;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Represents a server which Web-Browser can connect to and communicate via HTTP.
 *
 */
public class GameServer {

    private SSLServerSocketFactory socketFactory;
    private KeyManagerFactory keyManagerFactory;
    private KeyStore keyStore;
    private char[] passWord = "rootroot".toCharArray();
    private InputStream inputStream;
    private SSLContext context;
    private SSLServerSocket serverSocket;
    private int portNumber = 443;


    GameServer() throws IOException, NoSuchProviderException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {

        this.inputStream = new FileInputStream(new File("src/extra/.keystore"));
        this.keyStore = KeyStore.getInstance("JKS", "SUN");
        this.keyStore.load(this.inputStream, this.passWord);
        this.context = SSLContext.getInstance("TLS");
        this.keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        this.keyManagerFactory.init(this.keyStore, this.passWord);
        this.context.init(this.keyManagerFactory.getKeyManagers(), null, null);
        this.socketFactory = context.getServerSocketFactory();

    }
    /**
     * Initiates the server with the given port number.
     */
    private void initiateServer() throws IOException{

            this.serverSocket = (SSLServerSocket)this.socketFactory.createServerSocket(this.portNumber);
            System.out.println("The server has been created");

            while (true){
                SSLSocket socket = (SSLSocket) this.serverSocket.accept();
                GameSession session = new GameSession(socket);
                Thread thread = new Thread(session);
                thread.start();
            }
    }

    /**
     * Called upon when the program is executed.
     * @param args is a set of arguments in the form of an array of strings sent via the command line.
     */
    public static void main(String[] args){

        try {
            GameServer server = new GameServer();
            server.initiateServer();
        } catch (IOException | NoSuchProviderException | KeyStoreException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
        }

    }
}
