/**
 * Author: Fredrik Öberg
 * Date of Creation: 201110
 * Date of Latest Update: -
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Handles HTTP requests sent via the a TCP socket and interprets the request.
 */
public class HTTPHandler {

        private final PrintWriter sender;
        private final BufferedReader receiver;
        private final HTMLHandler html;
        private final StringBuilder stringBuilder;

    /**
     * A constructor.
     *
      * @param sender is responsible for sending data via a TCP socket.
     * @param receiver is responsible for receiving data via a TCP socket.
     */
    public HTTPHandler(PrintWriter sender, BufferedReader receiver) {
            this.sender = sender;
            this.receiver = receiver;
            this.html = new HTMLHandler();
            this.stringBuilder = new StringBuilder();
    }

    /**
     * Reads the first line of a HTTP request and checks if it is valid.
     * @return is the what type of a HTTP request which was received in the form of a String.
     */
    String validateRequest() {
        try {
            String request = this.receiver.readLine();
            String[] line = request.split("[ \n\r?]+");

            if(3 != line.length)
                return "BAD_REQUEST";
            else if (line[0].equals("GET") && (line[1].equals("/") ) && line[2].equals("HTTP/1.1"))
                return "GET";
            else if (line[0].equals("POST") && line[1].equals("/") && line[2].equals("HTTP/1.1"))
                return "POST";
            else
                return "BAD_REQUEST";

        } catch (IOException e) {
            e.printStackTrace();
            return "BAD_REQUEST";
        }
    }

    /**
     * Gets the specified header option from the current HTTP request.
     * @param option is the option of interest.
     * @param header is the header of the current HTTP request.
     * @return is the value of the wanted option or no concatenated with
     *         the option if the option does not exist in the header.
     */
    private String getHeaderOption(String option, String[] header){

        for (int i = 0; i < header.length; i++)
            if (header[i].equals(option))
                return header[i + 1];

            return "no" + option;
    }

    /**
     * Returns the option of interest when a GET request has been made to the server.
     * @return is the requests cookie or noCookie if there exists no cookie.
     */
    String getGETHeader(){
       try {
           String[] header = getHeaders();

           return getHeaderOption("Cookie:", header);

       }catch (IOException e){
           return "noCookie:";
       }
    }

    /**
     * Collects the headers of the received HTTP request.
     * @return is the headers of the request in the form of an array of Strings.
     * @throws IOException is thrown if the socket could not collect the headers.
     */
    private String[] getHeaders() throws IOException{
        try {
            String header;

            while (!(header = this.receiver.readLine()).equals(""))
                this.stringBuilder.append(header).append("\n");


            return this.stringBuilder.toString().split("[ \n]+");

        }catch (IOException e){
            e.printStackTrace();
            throw new IOException();
        }
    }

    /**
     * Collects the content length and the cookie in the case of a HTTP POST
     * request is received by the server.
     * @return is the POST headers of interest.
     * @throws NoSuchFieldException if the headers of interest does not exist.
     */
    String[] getPostData() throws NoSuchFieldException{

        try {
            String[] header = getHeaders();
            String[] postHeaders = new String[2];

            postHeaders[0] = getHeaderOption("Content-Length:", header);
            postHeaders[1] = getHeaderOption("Cookie:", header);

            return postHeaders;

        }catch (IOException e){
            throw new NoSuchFieldException();
        }
    }

    /**
     * Collects the body of the received POST request.
     *
     * @param contentLength is the length of the POST requests body.
     * @return is the data in the POST body in the form of a string.
     * @throws IOException if the body of the data could not be read.
     */
    String getPostBody(String contentLength) throws IOException {
        this.stringBuilder.setLength(0);

        for(int i = 0; i < Integer.parseInt(contentLength); i++)
            stringBuilder.append((char)this.receiver.read());

        return this.stringBuilder.toString();
    }

    /**
     * Sends a OK response when a correct guess has been made.
     * @param guesses is the final amount of guesses made in the game session
     * @param cookie is the cookie of the next game session.
     */
    void sendCorrectGuessResponse(int guesses, String cookie){
            sendOKResponse(this.html.getCorrectAnswer(guesses), cookie);
    }

    /**
     * Sends a OK response when a to high guess has been made.
     * @param guesses is the current amount of guesses made in the game session
     * @param cookie is the cookie of the current game session.
     */
    void sendHighGuessResponse(int guesses, String cookie){
            sendOKResponse(this.html.getHighAnswer(guesses), cookie);
    }

    /**
     * Sends a OK response when a low guess has been made.
     * @param guesses is the current amount of guesses made in the game session
     * @param cookie is the cookie of the current game session.
     */
    void sendLowGuessResponse(int guesses, String cookie){
            sendOKResponse(this.html.getLowAnswer(guesses), cookie);
    }

    /**
     * Sends a OK response when an incorrect input has been made.
     * @param guesses is the current amount of guesses made in the game session
     * @param cookie is the cookie of the current game session.
     */
    void sendIncorrectInputResponse(int guesses, String cookie){
          sendOKResponse(this.html.getIncorrectInput(guesses), cookie);
    }

    /**
     * Sends a OK response when a request from a client without a
     * session-cookie has been correctly received by the server.
     * @param cookie is the cookie of the current game session.
     */
    void sendInitialResponse(String cookie){
            sendOKResponse(this.html.getInitialHTML(), cookie);
    }

    /**
     * Sends a bad request response to the client if an incorrect request has been made.
     */
    void sendBadRequestResponse(){

            String body = this.html.getBadRequestHtml();
            byte[] responseBuffer = body.getBytes();
            int contentLength = responseBuffer.length;

            this.sender.println("HTTP/1.1 400 Bad Request");
            this.sender.println("Content-Length: " + contentLength);
            this.sender.println("");
            this.sender.println(body);
            this.sender.flush();
    }

    /**
     * Sends an OK response when a correct HTTP request has been made.
     * @param body is the body of the HTTP response.
     * @param cookie is the cookie of the current game session.
     */
    private void sendOKResponse(String body, String cookie){

            byte[] responseBuffer = body.getBytes();
            int contentLength = responseBuffer.length;

            this.sender.println("HTTP/1.1 200 OK");
            this.sender.println("Content-Length: " + contentLength);
            this.sender.println("Set-Cookie: " + cookie);
            this.sender.println("");
            this.sender.println(body);
            this.sender.flush();
    }
}
