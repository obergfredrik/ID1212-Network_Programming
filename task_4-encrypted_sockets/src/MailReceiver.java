import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MailReceiver {

        private SSLSocketFactory socketFactory;
        private SSLSocket socket;
        private PrintWriter writer;
        private BufferedReader reader;
        private StringBuilder stringBuilder;

        MailReceiver(){
            this.socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            HttpsURLConnection.setDefaultSSLSocketFactory(this.socketFactory);
        }

        void getServerResponse(String tag) throws IOException{

            System.out.println();
            this.stringBuilder.setLength(0);
            String line;
            String[] response;

            do {
                line = this.reader.readLine();
                this.stringBuilder.append(line + "\n");
                response = line.split(" ");

            }while (!(response[0].equals(tag) && (response[1].equals("OK") || response[1].equals("BAD") || response[1].equals("NO") || response[1].equals("PREAUTH") || response[1].equals("BYE"))));

            System.out.println(this.stringBuilder.toString());
        }

        void initiateConnection() throws IOException {

            this.socket = (SSLSocket)this.socketFactory.createSocket(Constants.IMAP_HOST, Constants.IMAP_PORT);
            this.writer = new PrintWriter(this.socket.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.socket.setEnabledCipherSuites(Constants.CIPHER);
            this.stringBuilder = new StringBuilder();

        }

        void getBody(String tag)throws IOException{

            this.writer.println(tag + " fetch 2 body[text]\r\n");
            this.writer.flush();
            getServerResponse(tag);

        }

        void selectInbox(String tag)throws IOException{

            this.writer.println(tag + " select inbox\r\n");
            this.writer.flush();

            getServerResponse(tag);

        }

        void login(String tag) throws IOException {

            this.writer.println(tag + " " + "LOGIN" + " " + Constants.USERNAME +" " + Constants.PASSWORD + "\r\n");
            this.writer.flush();
            getServerResponse(tag);

        }

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
