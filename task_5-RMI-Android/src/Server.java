/**
 * Created by: Fredrik Öberg
 * Date of creation: 201205
 */

import com.sun.mail.imap.*;
import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import javax.mail.*;
import java.util.Properties;

/**
 *  A distributed application which connects to a given IMAP server and retrieves the most recent mail from the inbox. The class
 *  implements an interface extending the remote class so the Remote Method Invocation(RMI) protocol can be used so that a client
 *  connecting to the server can invoke the "fetchMail" method without knowing its internal structure.
 */
public class Server implements Mail {


    /**
     * A constructor.
     */
    public Server() {}

    /**
     * Called for via the Mail interface returning the most recent mail from the given IMAP server.
     *
     * @return is the most recent mail.
     * @throws MessagingException when the messages do not work as intended.
     * @throws IOException if there has been som issues with the input or output stream.
     */
    @Override
    public String fetchIMAPMail(String host, String userName, String passWord, int port) throws MessagingException, IOException {

        Store store = initiateIMAPConnection(host,userName, passWord, port);
        Message message = getFirstMessage(store);

        return extractMail(message);
    }

    /**
     * Creates the mail in the form of a String from the given message parameter.
     *
     * @param message is the most recent mail in the inbox.
     * @return is the most recent mail.
     * @throws MessagingException when the messages do not work as intended.
     * @throws IOException if there has been som issues with the input or output stream.
     */
    private String extractMail(Message message) throws MessagingException, IOException {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n");
        stringBuilder.append("From: " + message.getFrom()[0] + "\n");
        stringBuilder.append("Subject: " + message.getSubject() + "\n\n");
        stringBuilder.append(message.getContent().toString());

        return stringBuilder.toString();
    }

    /**
     * Retrieves the first message from the inbox of the connected users IMAP account.
     *
     * @param store contains the messages of the users IMAP account.
     * @return is the most recently received message retrieved from the store parameter.
     * @throws MessagingException when the messages do not work as intended.
     */
    private Message getFirstMessage(Store store) throws MessagingException {

        IMAPStore imapStore = (IMAPStore) store;
        IMAPFolder inbox = (IMAPFolder) imapStore.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();

        return messages[messages.length - 1];
    }

    /**
     * Initiates a connection to a given IMAP server and retrieves the messages from the users
     * IMAP account and stores them in a Store object.
     *
     * @return is the users messages in the form of a <>Store</> object.
     * @throws MessagingException when the messages do not work as intended.
     */
    private Store initiateIMAPConnection(String host, String userName, String passWord, int port) throws MessagingException {

        Session emailSession = Session.getDefaultInstance(getIMAPProperties(host, port), null);
        Store store = emailSession.getStore("imap");
        store.connect(host,port, userName, passWord);

        return store;
    }

    /**
     * Initializes a <>Properties</> object containing all the information needed to connect and
     * log in to a IMAP server.
     *
     * @return is the properties object containing information such as username and password.
     */
    private Properties getIMAPProperties(String host, int port){

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.host", host);
        properties.put("mail.imap.port", "" + port);
        properties.put("mail.imap.ssl.enable", "true");

        return properties;
    }

    /**
     * Called when the program is being executed.
     *
     * @param args contains arguments entered via the command line.
     */
    public static void main(String[] args) {

        try {
            Server server = new Server();
            Mail stub = (Mail) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(1234);

            registry.bind("Mail", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
