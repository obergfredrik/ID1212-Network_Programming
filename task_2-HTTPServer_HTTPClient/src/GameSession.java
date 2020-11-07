import java.io.*;
import java.net.Socket;
import java.util.Random;

public class GameSession implements Runnable{

    private final Socket socket;
    private BufferedReader receiver;
    private PrintWriter sender;
    private int guesses = 0;
    private int randomValue;

    public GameSession(Socket socket) {
        this.socket = socket;
        Random random = new Random();
        this.randomValue = random.nextInt(101);
        System.out.println("The generated number is: " + this.randomValue);

        try {
            InputStream inputStream = this.socket.getInputStream();
            this.receiver = new BufferedReader(new InputStreamReader(inputStream));
            this.sender = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

            HTTPHandler httpHandler = new HTTPHandler(this.sender);
            StringBuilder stringBuilder = new StringBuilder();
            HTMLHandler html = new HTMLHandler();
            int contentLength;
            int guessValue;


        try {

            String line = this.receiver.readLine();

            if (httpHandler.validateGETRequest(line)) {
                    httpHandler.sendOKResponse(html.getInitialHTML());
                }
                else {
                    httpHandler.sendBadRequestResponse(html.getBadRequestHtml());
                    this.socket.close();
                }

            while (!this.receiver.readLine().equals("")){}


            while(true){

                line = this.receiver.readLine();
                System.out.println(line);

                if(!httpHandler.validatePOSTRequest(line)) {
                    httpHandler.sendBadRequestResponse(html.getBadRequestHtml());
                    break;
                }

                while (!(line = this.receiver.readLine()).equals(""))
                     stringBuilder.append(line).append("\n");


                try {


                    contentLength = httpHandler.getContentLength(stringBuilder.toString());

                    if (-1 != contentLength) {
                        stringBuilder.setLength(0);
                        for (int i = 0; i < contentLength; i++)
                            stringBuilder.append((char)this.receiver.read());
                    }

                   try {
                        guessValue = httpHandler.extractGuessValue(stringBuilder.toString());

                        this.guesses++;

                        if(guessValue == this.randomValue) {
                            httpHandler.sendGuessResponse(html.correctAnswer(this.guesses));
                            this.socket.close();
                            break;
                        }
                        else if (guessValue > this.randomValue)
                             httpHandler.sendGuessResponse(html.highAnswer(this.guesses));
                        else
                            httpHandler.sendGuessResponse(html.lowAnswer(this.guesses));

                   }catch (NumberFormatException e){

                       httpHandler.sendGuessResponse(html.incorrectInput(this.guesses));

                   }

                }catch (NoSuchFieldException e){
                    System.out.println(e);
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}
