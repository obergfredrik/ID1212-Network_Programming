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
    private StringBuilder stringBuilder;


    MailSender(){
        this.socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        HttpsURLConnection.setDefaultSSLSocketFactory(this.socketFactory);
    }

    void sendMessage(String message)throws IOException{

        this.writer.println("HELO " + Constants.SMTP_HOST);
        this.writer.flush();
        System.out.println(this.reader.readLine());

    }

    void initiateConnection()throws IOException {

        this.socket = new Socket(Constants.SMTP_HOST, Constants.SMTP_PORT);
        this.writer = new PrintWriter(this.socket.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        System.out.println(this.reader.readLine());
        this.writer.println("HELO " + Constants.SMTP_HOST);
        this.writer.flush();
        System.out.println(this.reader.readLine());
        this.writer.println("STARTTLS");
        this.writer.flush();
        System.out.println(this.reader.readLine());

        this.SSLsocket = (SSLSocket) this.socketFactory.createSocket( this.socket, this.socket.getInetAddress().getHostAddress(), this.socket.getPort(), true);
        this.writer = new PrintWriter(this.SSLsocket.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(this.SSLsocket.getInputStream()));

        this.writer.println("HELO " + Constants.SMTP_HOST);
        this.writer.flush();
        System.out.println(this.reader.readLine());

        this.writer.println("AUTH LOGIN");
        this.writer.flush();
        System.out.println(this.reader.readLine());

        byte[] decodedBytes = Base64.getDecoder().decode("VXNlcm5hbWU6");
        String decodedString = new String(decodedBytes);
        System.out.println(decodedString);

        String encodedString = Base64.getEncoder().encodeToString(Constants.USERNAME.getBytes());

        this.writer.println(encodedString);
        this.writer.flush();
        System.out.println(this.reader.readLine());

        byte[] decodedBytes2 = Base64.getDecoder().decode("UGFzc3dvcmQ6");
        String decodedString2 = new String(decodedBytes2);
        System.out.println(decodedString2);

        encodedString = Base64.getEncoder().encodeToString(Constants.PASSWORD.getBytes());

        this.writer.println(encodedString);
        this.writer.flush();
        System.out.println(this.reader.readLine());

        this.writer.println("MAIL FROM:<fobe@kth.se>");
        this.writer.flush();
        System.out.println(this.reader.readLine());

        this.writer.println("RCPT TO:<fobe@kth.se>");
        this.writer.flush();
        System.out.println(this.reader.readLine());

        this.writer.println("DATA");
        this.writer.flush();
        System.out.println(this.reader.readLine());

        this.writer.println("hej det här är jag från intellij");
        this.writer.println(".");
        this.writer.flush();
        System.out.println(this.reader.readLine());

        this.writer.println("QUIT");
        this.writer.flush();
        System.out.println(this.reader.readLine());

    }

    public static void main(String[] args){

        MailSender mailSender = new MailSender();

        try {
            mailSender.initiateConnection();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
