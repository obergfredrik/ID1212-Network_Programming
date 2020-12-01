/**
 * Author: Fredrik Öberg
 * Date of Creation: 201201
 * Date of Latest Update: -
 *
 */

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

/**
 * Logs in to a given SMTP server and sends a mail to the same inbox as it was sent from.
 */
public class MailSender {

    /**
     * Class attributes.
     */
    private SSLSocketFactory socketFactory;
    private Socket socket;
    private SSLSocket SSLsocket;
    private PrintWriter writer;
    private BufferedReader reader;

    /**
     * A constructor.
     */
    MailSender(){
        this.socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        HttpsURLConnection.setDefaultSSLSocketFactory(this.socketFactory);
    }

    /**
     * Sends a given message the SMTP server.
     *
     * @param message is the message being sent.
     * @throws IOException is thrown if there has been some issues with the sent message.
     */
    void sendMessage(String message)throws IOException{

        this.writer.println(message);
        this.writer.flush();
        System.out.println(this.reader.readLine());

    }

    /**
     * Transforms the connection to the SMTP server into an encrypted secure connection using TLS.
     *
     * @throws IOException is thrown if there has been some issues with the input or output stream.
     */
    void initiateTLSSession()throws IOException{

        this.SSLsocket = (SSLSocket) this.socketFactory.createSocket( this.socket, this.socket.getInetAddress().getHostAddress(), this.socket.getPort(), true);
        this.writer = new PrintWriter(this.SSLsocket.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(this.SSLsocket.getInputStream()));

    }

    /**
     * Encodes a message into an Base&4 encode String.
     *
     * @param message is the message being encoded in the form of an array of type <>byte</>.
     * @return is the Base64 encoded message.
     */
    String encodeString(byte[] message){
        return Base64.getEncoder().encodeToString(message);
    }

    /**
     * Initiates a unencrypted session to the SMTP server.
     *
     * @throws IOException is thrown if there has been some issues with the input or output stream.
     */
    void initiateConnection()throws IOException {

        this.socket = new Socket(Constants.SMTP_HOST, Constants.SMTP_PORT);
        this.writer = new PrintWriter(this.socket.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        System.out.println(this.reader.readLine());
    }

    /**
     * Is called when the program is executed.
     *
     * @param args is a set of arguments entered via the command line in the form of an array of type <>String</>.
     */
    public static void main(String[] args){

        MailSender mailSender = new MailSender();

        try {
            mailSender.initiateConnection();
            mailSender.sendMessage("HELO " + Constants.SMTP_HOST);
            mailSender.sendMessage("STARTTLS");
            mailSender.initiateTLSSession();
            mailSender.sendMessage("HELO " + Constants.SMTP_HOST);
            mailSender.sendMessage("AUTH LOGIN");
            mailSender.sendMessage(mailSender.encodeString(Constants.USERNAME.getBytes()));
            mailSender.sendMessage(mailSender.encodeString(Constants.PASSWORD.getBytes()));
            mailSender.sendMessage("MAIL FROM:" + Constants.MAIL);
            mailSender.sendMessage("RCPT TO:" + Constants.MAIL);
            mailSender.sendMessage("DATA");
            mailSender.sendMessage("I can send whatever i want. Mwahahahaha!\r\n.");
            mailSender.sendMessage("QUIT");

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
