/**
 * Created by: Fredrik Öberg
 * Date of creation: 201205
 */

import javax.mail.MessagingException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Calls for a fetchMail method a server has implemented via the Remote Method Invocation(RMI) protocol.
 * It thereby receives the most recent mail from a given IMAP account.
 */
public class Client {

    /**
     * A constructor.
     */
    private Client() {}


    /**
     * Called when the program is being executed.
     *
     * @param args contains arguments entered via the command line.
     */
    public static void main(String[] args) {

        try {

            Registry registry = LocateRegistry.getRegistry("localhost", 1234);
            Mail stub = (Mail) registry.lookup("Mail");
            String response = stub.fetchIMAPMail(Constants.IMAP_HOST, Constants.USERNAME, Constants.PASSWORD, Constants.IMAP_PORT);
            System.out.println("\n***MAIL RECEIVED***\n " + response);
        } catch (MessagingException | IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
