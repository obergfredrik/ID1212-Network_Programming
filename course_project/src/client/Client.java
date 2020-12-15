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
    private boolean loggedIn;
    private boolean sending;
    private boolean receiving;
    private boolean serverIsReceiving;
    private boolean serverIsSending;

    Client() throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        File cert = new File("src/client/server.crt");
        Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(cert));
        KeyStore keyStore = createKeyStore(certificate);
        TrustManagerFactory trustManagerFactory = createTrustManagerFactory(keyStore);
        SSLSocketFactory socketFactory = createSSLSocketFactory(trustManagerFactory);

        this.socket = (SSLSocket) socketFactory.createSocket(this.hostName, this.portNumber);
    }

    private SSLSocketFactory createSSLSocketFactory(TrustManagerFactory trustManagerFactory) throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);

        return socketFactory;
    }

    private TrustManagerFactory createTrustManagerFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        return trustManagerFactory;
    }

    private KeyStore createKeyStore(Certificate certificate) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("server", certificate);

        return keyStore;
    }

    /**
     * Creates a sock from which a tcp session can be initiated. Two threads are also
     * created; one for sending messages and one for receiving.
     */
    private void createClient(){

        this.sender = new Thread(new Sender(this.socket, this));
        this.receiver = new Thread(new Receiver(this.socket, this));
        this.sender.start();
        this.receiver.start();
        this.loggedIn = false;
        this.sending = false;
        this.receiving = false;
        this.serverIsReceiving = false;
        this.serverIsSending = false;
    }

    void setLoggedIn(boolean loggedIn){
        this.loggedIn = loggedIn;
    }

    boolean isLoggedIn(){
        return this.loggedIn;
    }

    public boolean isSending() {
        return sending;
    }

    public void setSending(boolean sending) {
        this.sending = sending;
    }

    public boolean isReceiving() {
        return receiving;
    }

    public void setReceiving(boolean receiving) {
        this.receiving = receiving;
    }

    public boolean isServerIsReceiving() {
        return serverIsReceiving;
    }

    public void setServerIsReceiving(boolean serverIsReceiving) {
        this.serverIsReceiving = serverIsReceiving;
    }

    public boolean isServerIsSending() {
        return serverIsSending;
    }

    public void setServerIsSending(boolean serverIsSending) {
        this.serverIsSending = serverIsSending;
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


