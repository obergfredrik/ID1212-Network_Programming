/**
 * Created by: Fredrik Öberg
 * Date of creation: 201205
 */

import javax.mail.MessagingException;
import java.io.IOException;
import java.rmi.Remote;

/**
 * An interface used when a class want to implement and use the Remote Method Invocation(RMI)
 */
public interface Mail extends Remote {
    String fetchIMAPMail(String host, String userName, String passWord, int port) throws MessagingException, IOException;
}
