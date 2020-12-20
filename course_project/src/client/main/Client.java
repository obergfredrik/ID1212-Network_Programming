/**
 *  Created by: Fredrik Öberg
 *  Date of creation: 201216
 *  Latest update: -
 *
 */

package client.main;

import client.service.Receiver;
import client.service.Sender;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Represents an instance of the chat client which connects to a chat server using Transport Layer Security.
 * The user of the client can send chat messages as well as files to other connected clients.
 */
public class Client {

    /**
     * Class attributes.
     */
    private final SSLSocket socket;
    private boolean loggedIn;
    private boolean sending;
    private boolean receiving;
    private boolean serverTransferring;

    /**
     * A constructor.
     *
     * @throws IOException when the certificate file could not be read.
     * @throws CertificateException when a certificate could not be generated.
     * @throws KeyStoreException when a keystore could not be created.
     * @throws NoSuchAlgorithmException when either a KeyStore, TrustManagerFactory or a SSLContext could not be created.
     * @throws KeyManagementException when a SSLContext could not be initiated.
     */
    private Client() throws IOException, CertificateException, KeyStoreException, KeyManagementException, NoSuchAlgorithmException {

        String hostName = "localhost";
        int portNumber = 1234;

        File cert = new File("src/client/server.crt");
        Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(cert));
        KeyStore keyStore = createKeyStore(certificate);
        TrustManagerFactory trustManagerFactory = createTrustManagerFactory(keyStore);
        SSLContext sslContext = createSSLContext(trustManagerFactory);
        SSLSocketFactory socketFactory = createSSLSocketFactory(sslContext);

        this.socket = (SSLSocket) socketFactory.createSocket(hostName, portNumber);
    }

    /**
     * Creates a SSLSocketFactory.
     *
     * @param sslContext is the context for the SSLSocketFactory
     * @return is the created SSLSocketFactory.
     */
    private SSLSocketFactory createSSLSocketFactory(SSLContext sslContext) {

        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);

        return socketFactory;
    }

    /**
     * Creates a SSLContext.
     *
     * @param trustManagerFactory is used as argument when initializing the SSLContext.
     * @return is the created SSLContext.
     * @throws NoSuchAlgorithmException if there is no algorithm as specified in the getInstance method call.
     * @throws KeyManagementException if the SSLContext could not be initialized.
     */
    private SSLContext createSSLContext(TrustManagerFactory trustManagerFactory) throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

        return sslContext;

    }

    /**
     * Creates a TrustManagerFactory object.
     *
     * @param keyStore is used as argument when initializing the TrustManagerFactory object.
     * @return is the created TrustManagerFactory object.
     * @throws NoSuchAlgorithmException if no Provider supports a TrustManagerFactory implementation for the specified algorithm.
     * @throws KeyStoreException if the TrustManagerFactory could not be created.
     */
    private TrustManagerFactory createTrustManagerFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        return trustManagerFactory;
    }

    /**
     * Creates a KeyStore object.
     *
     * @param certificate is the certificate used when calling the KeyStore objects setCertificateEntry method.
     * @return is the created KeyStore.
     * @throws CertificateException if any of the certificates in the keystore could not be loaded.
     * @throws NoSuchAlgorithmException if the algorithm used to check the integrity of the keystore cannot be found.
     * @throws IOException if there is an I/O or format problem with the keystore data, if a password is required but not
     *         given, or if the given password was incorrect.
     * @throws KeyStoreException if no Provider supports a KeyStoreSpi implementation for the specified type or if the
     *         keystore has not been initialized, or the given alias already exists and does not identify an entry containing
     *         a trusted certificate, or this operation fails for some other reason..
     */
    private KeyStore createKeyStore(Certificate certificate) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("server", certificate);

        return keyStore;
    }

    /**
     * Creates a sender and a receiver thread and initialises them as well as setting the class attributes to their initial values.
     */
    private void initiateConnection(){

        Thread sender = new Thread(new Sender(this.socket, this));
        Thread receiver = new Thread(new Receiver(this.socket, this));
        sender.start();
        receiver.start();
        this.loggedIn = false;
        this.sending = false;
        this.receiving = false;
        this.serverTransferring = false;

    }

    /**
     * A setter for the loggedIn class attribute.
     *
     * @param loggedIn is the new value for the class attribute loggedIn.
     */
    public void setLoggedIn(boolean loggedIn){
        this.loggedIn = loggedIn;
    }

    /**
     * A getter for the class attribute loggedIn.
     *
     * @return is the current value of loggedIn.
     */
    public boolean isLoggedIn(){
        return this.loggedIn;
    }

    /**
     * A getter for the class attribute isSending.
     *
     * @return is the current value of isSending.
     */
    public boolean isSending() {
        return sending;
    }

    /**
     * A setter for the sending class attribute.
     *
     * @param sending is the new value for the class attribute sending.
     */
    public void setSending(boolean sending) {
        this.sending = sending;
    }

    /**
     * A getter for the class attribute receiving.
     *
     * @return is the current value of receiving.
     */
    public boolean isReceiving() {
        return receiving;
    }

    /**
     * A setter for the receiving class attribute.
     *
     * @param receiving is the new value for the class attribute receiving.
     */
    public void setReceiving(boolean receiving) {
        this.receiving = receiving;
    }

    /**
     * A getter for the class attribute serverTransferring.
     *
     * @return is the current value of serverTransferring.
     */
    public boolean isServerTransferring() {
        return serverTransferring;
    }

    /**
     * A setter for the serverTransferring class attribute.
     */
    public void setTransferring(boolean transferring) {
        serverTransferring = transferring;
    }

    /**
     * Called upon when the code is executed and initializes the chat client.
     *
     * @param args is the entered parameters in the form of an array of strings.
     */
    public static void main( String[] args) {
        Client client;

        try {
            client = new Client();
            client.initiateConnection();
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();

        }
    }
}


