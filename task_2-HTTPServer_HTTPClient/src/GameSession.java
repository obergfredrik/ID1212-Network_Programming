import java.io.*;
import java.net.Socket;
import java.util.Random;

public class GameSession implements Runnable{

    private final Socket socket;
    private BufferedReader receiver;
    private PrintWriter sender;
    private final Random random;
    private int guesses = 0;
    private int randomValue;
    private String lastGuess;

    public GameSession(Socket socket) {
        this.socket = socket;
        this.random = new Random();

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

            HTTPHandler httpHandler = new HTTPHandler(this.sender, this.receiver);
            String[] session;

        try {

                 switch (httpHandler.validateRequest()){
                     case "GET":
                         String cookie = httpHandler.getCookie();
                         if(cookie.equals("noCookie"))
                             httpHandler.sendInitialResponse(generateSession());
                         else {
                             session = extractSession(cookie);

                             if (session[2].equals("0"))
                                 httpHandler.sendInitialResponse(cookie);
                             else if (session[6].equals("HI"))
                                 httpHandler.sendHighGuessResponse(Integer.parseInt(session[2]), cookie);
                             else
                                 httpHandler.sendLowGuessResponse(Integer.parseInt(session[2]), cookie);
                         }
                         break;
                     case "POST":
                         String[] postData = httpHandler.getPostData();
                         String body = httpHandler.getPostBody(postData[0]);
                         session = extractSession(postData[1]);
                         this.guesses = Integer.parseInt(session[2]) + 1;
                         this.randomValue = Integer.parseInt(session[4]);
                         this.lastGuess = session[6];
                         int guessValue = extractGuessValue(body);

                         if (guessValue == this.randomValue)
                             httpHandler.sendCorrectGuessResponse(this.guesses, generateSession());
                         else if (guessValue > this.randomValue)
                             httpHandler.sendHighGuessResponse(this.guesses, "SessionId=guesses=" + this.guesses + "&random=" + this.randomValue + "&lastGuess=HI");
                         else
                             httpHandler.sendLowGuessResponse(this.guesses, "SessionId=guesses=" + this.guesses + "&random=" + this.randomValue + "&lastGuess=LO");
                         break;
                     case "BAD_REQUEST":
                         httpHandler.sendBadRequestResponse();
                         closeSocket();
                         break;
             }

            closeSocket();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException | NumberFormatException e) {
                e.printStackTrace();
                httpHandler.sendIncorrectInputResponse(this.guesses - 1,"SessionId=guesses="+ (this.guesses  - 1) + "&random=" + this.randomValue + "&lastGuess=" + this.lastGuess);
                closeSocket();
        }


    }

    String generateSession(){
        return "SessionId=guesses=0&random=" + this.random.nextInt(101) + "&lastGuess=NA";
    }

    String[] extractSession(String cookie){
        System.out.println(cookie);
        return cookie.split("[=&]+");
    }

    int extractGuessValue(String guess) throws NoSuchFieldException{
        try {
            String[] value = guess.split("=");

            if (2 != value.length)
                throw new NoSuchFieldException();

            int guessValue = Integer.parseInt(value[1]);

            if (guessValue < 0 || guessValue > 100)
                throw new NumberFormatException();

            return guessValue;
        }catch (NumberFormatException e){
            throw new NumberFormatException();
        }
    }

    private void closeSocket(){
        try {
            this.socket.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
