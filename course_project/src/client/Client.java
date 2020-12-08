package client; /**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201101
 *  Latest update: -
 *
 */


import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Represents an instance of the chat client which connects to a chat server
 * where the user of the client can sen chat messages to other connected clients.
 */
public class Client {

    private SSLSocket socket;
    private Thread sender;
    private Thread receiver;
    private String hostName = "localhost";
    private int portNumber = 1234;

    Client() throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        File cert = new File("src/client/server.crt");Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(cert));
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("server", certificate);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
        this.socket = (SSLSocket) socketFactory.createSocket(this.hostName, this.portNumber);

    }

    /**
     * Creates a sock from which a tcp session can be initiated. Two threads are also
     * created; one for sending messages and one for receiving.
     */
    private void createClient(){

        this.sender = new Thread(new MessageSender(this.socket));
        this.receiver = new Thread(new MessageReceiver(this.socket));
        this.sender.start();
        this.receiver.start();

    }

    /**
     * Called upon when the code is executed.
     * @param args is the entered parameters in the form of an array of strings.
     */
    public static void main( String[] args) {
        Client client = null;

        try {
            client = new Client();
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }finally {
            client.createClient();
        }
    }
}


