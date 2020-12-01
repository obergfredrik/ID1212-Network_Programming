/**
 * Author: Fredrik Öberg
 * Date of Creation: 201110
 * Date of Latest Update: 201201
 *
 */

package extra;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.util.Random;

/**
 * Contains the code handling the current game session. Implements the runnable interface for multi-threading functionality.
 */

public class GameSession implements Runnable{

    private final SSLSocket socket;
    private BufferedReader receiver;
    private PrintWriter sender;
    private final Random random;
    private int guesses = 0;
    private int randomValue;
    private String lastGuess;

    /**
     * A GameSession constructor.
     * @param socket is the socket receiving a TCP stream of data for the game session.
     */
    public GameSession(SSLSocket socket) {
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

    /**
     * Is initiated when a connection has been made from a client to the server and the start function of the
     * current thread has been called upon. Handles the logic of a game session.
     */
    @Override
    public void run() {

            HTTPHandler httpHandler = new HTTPHandler(this.sender, this.receiver);
            String[] sessionData;

        try {
                 switch (httpHandler.validateRequest()){
                     case "GET":
                         String sessionCookie = httpHandler.getGETHeader();
                         if(sessionCookie.equals("noCookie:"))
                             httpHandler.sendInitialResponse(generateSession(0, generateRandomNumber(), "NA"));
                         else {
                             sessionData = extractSessionData(sessionCookie);

                             if (sessionData[2].equals("0"))
                                 httpHandler.sendInitialResponse(sessionCookie);
                             else if (sessionData[6].equals("HI"))
                                 httpHandler.sendHighGuessResponse(Integer.parseInt(sessionData[2]), sessionCookie);
                             else

                                 httpHandler.sendLowGuessResponse(Integer.parseInt(sessionData[2]), sessionCookie);
                         }
                         break;
                     case "POST":
                         String[] postData = httpHandler.getPostData();
                         String body = httpHandler.getPostBody(postData[0]);
                         sessionData = extractSessionData(postData[1]);
                         this.guesses = Integer.parseInt(sessionData[2]) + 1;
                         this.randomValue = Integer.parseInt(sessionData[4]);
                         this.lastGuess = sessionData[6];
                         int guessValue = extractGuessValue(body);

                         if (guessValue == this.randomValue)
                             httpHandler.sendCorrectGuessResponse(this.guesses, generateSession(0, generateRandomNumber(),"NA"));
                         else if (guessValue > this.randomValue)
                             httpHandler.sendHighGuessResponse(this.guesses, generateSession( this.guesses ,this.randomValue ,"HI"));
                         else
                             httpHandler.sendLowGuessResponse(this.guesses, generateSession(this.guesses ,this.randomValue, "LO"));
                         break;
                     case "BAD_REQUEST":
                         httpHandler.sendBadRequestResponse();
                         closeSocket();
                         break;
             }
            closeSocket();

            } catch (IOException e) {
                e.printStackTrace();
                closeSocket();
            } catch (NoSuchFieldException | NumberFormatException e) {
                e.printStackTrace();
                httpHandler.sendIncorrectInputResponse(this.guesses - 1, generateSession(this.guesses  - 1, this.randomValue , this.lastGuess));
                closeSocket();
        }
    }

    /**
     * Generates a new game session stored in the form of a HTTP cookie sent via a TCP stream.
     * @return is the game session in the form of a string.
     */
    String generateSession(int guesses, int randomValue, String lastGuess){
        return "SessionId=guesses="+ guesses + "&random=" + randomValue + "&lastGuess=" +lastGuess;
    }

    private int generateRandomNumber(){
        return this.random.nextInt(101);
    }
    /**
     * Splits a cookie game session on specified markers to extract the game session data stored in the cookie.
     * @param cookie if the cookie it raw untreated form.
     * @return is the cookie game session split into an array of Strings
     */
    String[] extractSessionData(String cookie){
        return cookie.split("[=&]+");
    }

    /**
     * Extract the guess value stored in the clients POST request body.
     * @param guess is the raw form of the POST body.
     * @return is the extracted post body data in the form of a <String\> object.
     * @throws NoSuchFieldException is thrown if the entered data in the POST request is
     *         not a number between 0 and 100.
     */
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

    /**
     * Closes the socket of the current thread
     */
    private void closeSocket(){
        try {
            this.socket.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
