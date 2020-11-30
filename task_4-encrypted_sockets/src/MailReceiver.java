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

        MailReceiver(){
            this.socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            HttpsURLConnection.setDefaultSSLSocketFactory(this.socketFactory);
        }

        void initiateConnection() throws IOException {

            this.socket = (SSLSocket)this.socketFactory.createSocket(Constants.HOST, Constants.PORT);
            this.writer = new PrintWriter(this.socket.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.socket.setEnabledCipherSuites(Constants.CIPHER);

            System.out.println();
            System.out.println(this.reader.readLine());


            writer.println("tag3 fetch 1 body\r\n");
            writer.flush();
//
//            System.out.println(this.reader.readLine() + "\n");
//            System.out.println(this.reader.readLine());
//            System.out.println(this.reader.readLine());
//            System.out.println(this.reader.readLine());
//            System.out.println(this.reader.readLine());
//            System.out.println(this.reader.readLine());
//            System.out.println(this.reader.readLine());
//            System.out.println(this.reader.readLine());
//            System.out.println(this.reader.readLine());




        }

        void getBody()throws IOException{

            this.writer.println("tag3 fetch 1 body[text]\r\n");
            this.writer.flush();
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());

            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());

            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
//            System.out.println(this.reader.readLine());
        }

        void selectInbox()throws IOException{
            this.writer.println("tag2 select inbox\r\n");
            this.writer.flush();
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println(this.reader.readLine());
            System.out.println("Select completed!");

        }

        void login() throws IOException {

            this.writer.println("tag1 LOGIN " + Constants.USERNAME +" " + Constants.PASSWORD + "\r\n");
            this.writer.flush();
            System.out.println(this.reader.readLine());
            System.out.println("Login completed!");

        }



        public static void main(String[] args)  {

            try {
                MailReceiver mailReceiver = new MailReceiver();
                mailReceiver.initiateConnection();
                mailReceiver.login();
                mailReceiver.selectInbox();
                mailReceiver.getBody();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


}
