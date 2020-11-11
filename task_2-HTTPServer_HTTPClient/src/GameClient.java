/**
 * Author: Fredrik Öberg
 * Date of Creation: 201111
 * Date of Latest Update: -
 *
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Connects to the guessing game server and finished a given number of game
 * sessions and outputs the average number of guesses it took to finish the games.
 */
public class GameClient {

    private String URL = "http://localhost:1234";
    private final int SESSIONS = 500;
    private int high;
    private int low;


    /**
     * Executes a number of game sessions specified in the object property SESSIONS and
     * calculates the average number of guesses it took to guess the right number and
     * prints it out to the standard output.
     */
    private void calculateAverageGuesses() {

        int[] guesses = new int[SESSIONS];

        for (int i = 0; i < this.SESSIONS; i++)
            guesses[i] = getGuesses();

        double total = 0;

        for (int i = 0; i < this.SESSIONS; i++)
            total = total + guesses[i];

        System.out.println("The average number of guesses was: " + (total/this.SESSIONS));
    }


    /**
     * Creates and processes a guessing game session and keeps track of how many
     * guesses it takes for the program to guess correctly.
     * @return is the amount of guesses it took to answer correctly during the
     *         current game session.
     */
    private int getGuesses(){
        int guesses = 0;

        try {
            URL url = new URL(this.URL);
            this.high = 101;
            this.low = 0;
            HttpURLConnection get = (HttpURLConnection) url.openConnection();

            if (200 == get.getResponseCode()) {

                List<String> cookie = extractCookie(get.getHeaderFields());
                String[] sessionData = extractSessionData(cookie.get(0));
                String session;
                String guess;
                byte[] postDataBytes;
                HttpURLConnection post;
                OutputStream output;

                do {
                    guess = generateGuess();
                    guesses++;
                    postDataBytes = guess.getBytes();
                    session = generateSession(Integer.parseInt(sessionData[2]), Integer.parseInt(sessionData[4]), sessionData[6]);
                    post =(HttpURLConnection) url.openConnection();
                    post.setDoOutput(true);
                    post.setRequestMethod("POST");
                    post.setRequestProperty("Cookie", session);
                    output = post.getOutputStream();
                    output.write(postDataBytes, 0, postDataBytes.length);
                    post.getResponseCode();
                    cookie = extractCookie(post.getHeaderFields());
                    sessionData = extractSessionData(cookie.get(0));
                    setNextGuess(sessionData[6]);
                    post.disconnect();
                }while (!sessionData[6].equals("NA"));
            }

            get.disconnect();

            return guesses;

        }catch (IOException e ){
            e.printStackTrace();
        }

        return guesses;
    }

    /**
     * Extracts the cookie from the HTTP response message received from the game server.
     * @param headers is a list of the response headers.
     * @return is the cookie header.
     */
    List<String> extractCookie(Map<String, List<String>> headers){
        return headers.get("Set-Cookie");
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
     * Generates a session in form of a String used in the Cookie POST header.
     * @param guesses is the current number of guesses in the session.
     * @param randomValue is the random value generated and is the goal of the guesses.
     * @param lastGuess is an indication of if the last gues in the session was to high or to low.
     * @return is the complete session information.
     */
    String generateSession(int guesses, int randomValue, String lastGuess){
        return "SessionId=guesses="+ guesses + "&random=" + randomValue + "&lastGuess=" +lastGuess;
    }

    /**
     * Generates the next guess value by using the high and low boundaries so that
     * the guess value can be something between 0 and 100.
     * @return is the generated guess value.
     */
    private String generateGuess(){
        return "guess=" + ((this.high + this.low)/2);
    }

    /**
     * Sets the next guess in the game session by updating the high and low boundaries
     * based on if the last guess was to high or to low.
     * @param lastGuess is the indication of if the last guess was to high or to low.
     */
    void setNextGuess(String lastGuess){

       if (lastGuess.equals("HI"))
            this.high = (this.high + this.low)/2;
       else if (lastGuess.equals("LO"))
            this.low = (this.high + this.low)/2;

    }

    /**
     * Called upon when the program is executed
     * @param args is program parameters entered via the command line.
     */
    public static void main(String[] args) {

            GameClient client = new GameClient();

            client.calculateAverageGuesses();

    }

}
