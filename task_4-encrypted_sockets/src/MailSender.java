import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class MailSender {

    private SSLSocketFactory socketFactory;
    private Socket socket;
    private SSLSocket SSLsocket;
    private PrintWriter writer;
    private BufferedReader reader;

    MailSender(){
        this.socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        HttpsURLConnection.setDefaultSSLSocketFactory(this.socketFactory);
    }

    void sendMessage(String message)throws IOException{

        this.writer.println(message);
        this.writer.flush();
        System.out.println(this.reader.readLine());

    }

    void initiateTLSSession()throws IOException{

        this.SSLsocket = (SSLSocket) this.socketFactory.createSocket( this.socket, this.socket.getInetAddress().getHostAddress(), this.socket.getPort(), true);
        this.writer = new PrintWriter(this.SSLsocket.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(this.SSLsocket.getInputStream()));

    }

    String encodeString(byte[] message){
        return Base64.getEncoder().encodeToString(message);
    }

    void initiateConnection()throws IOException {

        this.socket = new Socket(Constants.SMTP_HOST, Constants.SMTP_PORT);
        this.writer = new PrintWriter(this.socket.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        System.out.println(this.reader.readLine());
    }

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
