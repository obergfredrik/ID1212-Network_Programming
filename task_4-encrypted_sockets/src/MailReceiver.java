/**
 * Author: Fredrik Öberg
 * Date of Creation: 201201
 * Date of Latest Update: -
 *
 */


import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static java.util.Objects.isNull;


/**
 * Logs in to a IMAP host and retrieves the first mail of the users inbox.
 */
public class MailReceiver {

    /**
     * The MarlReceivers class attributes.
     */
    private SSLSocketFactory socketFactory;
        private SSLSocket socket;
        private PrintWriter writer;
        private BufferedReader reader;
        private StringBuilder stringBuilder;

    /**
     * A constructor.
     */
    MailReceiver(){
            this.socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            HttpsURLConnection.setDefaultSSLSocketFactory(this.socketFactory);
        }

    /**
     * Retrieves the response from the IMAP server and prints it out.
     *
     * @param tag is the request tag identifying the order of the messages.
     * @throws IOException is thrown when there has been som issues with the retrieved message.
     */
    void getServerResponse(String tag) throws IOException{

            this.stringBuilder.setLength(0);
            String line;
            String[] response;

            do {
                line = this.reader.readLine();
                this.stringBuilder.append(line + "\n");
                response = line.split(" ");

            }while (response.length == 0 || !(response[0].equals(tag) && (response[1].equals("OK") || response[1].equals("BAD") || response[1].equals("NO") || response[1].equals("PREAUTH") || response[1].equals("BYE"))) );

            System.out.println(this.stringBuilder.toString());
        }

    /**
     * Initiates a connection with the given IMAP server.
     *
     * @throws IOException is thrown when there has been som issues with the input or output stream.
     */
    void initiateConnection() throws IOException {

            this.socket = (SSLSocket)this.socketFactory.createSocket(Constants.IMAP_HOST, Constants.IMAP_PORT);
            this.writer = new PrintWriter(this.socket.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.socket.setEnabledCipherSuites(Constants.CIPHER);
            this.stringBuilder = new StringBuilder();

        }

    /**
     * Retrieves the body of the first mail on the users inbox.
     *
     * @param tag is the request tag identifying the order of the messages.
     * @throws IOException is thrown when there has been som issues with the sent message.
     */
    void getBody(String tag)throws IOException{

            this.writer.println(tag + " fetch 7 body[text]\r\n");
            this.writer.flush();
            getServerResponse(tag);

        }

    /**
     * Send the "select inbox" request to the IMAP server.
     *
     * @param tag is the request tag identifying the order of the messages.
     * @throws IOException is thrown when there has been som issues with the sent message.
     */
    void selectInbox(String tag)throws IOException{

            this.writer.println(tag + " SELECT INBOX\r\n");
            this.writer.flush();

            getServerResponse(tag);

        }

    /**
     * Sends the "login" request message to the IMAP server with the users name and password.
     *
     * @param tag is the request tag identifying the order of the messages.
     * @throws IOException is thrown when there has been som issues with the sent message.
     */
    void login(String tag) throws IOException {

            this.writer.println(tag + " " + "LOGIN" + " " + Constants.USERNAME +" " + Constants.PASSWORD + "\r\n");
            this.writer.flush();
            getServerResponse(tag);

        }

    /**
     * Is called for when the program is being executed.
     *
     * @param args is a set of arguments entered via the command lina in the form of an array of type <>String</>.
     */
        public static void main(String[] args)  {

            try {
                MailReceiver mailReceiver = new MailReceiver();
                mailReceiver.initiateConnection();
                mailReceiver.login("tag1");
                mailReceiver.selectInbox("tag2");
                mailReceiver.getBody("tag3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
