import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HTTPHandler {

        private final PrintWriter sender;
        private final BufferedReader receiver;
        private final HTMLHandler html;
        private final StringBuilder stringBuilder;

    public HTTPHandler(PrintWriter sender, BufferedReader receiver) {
            this.sender = sender;
            this.receiver = receiver;
            this.html = new HTMLHandler();
            this.stringBuilder = new StringBuilder();
    }

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

    String getCookie(){
        try {

            String options;

            while (!(options = this.receiver.readLine()).equals(""))
                this.stringBuilder.append(options).append("\n");

            String[] lines = this.stringBuilder.toString().split("[ \n]+");

            for (int i = 0; i < lines.length; i++)
                if (lines[i].equals("Cookie:"))
                    return lines[i + 1];

                return "noCookie";

        }catch (IOException e){
            e.printStackTrace();
            return "noCookie";
        }
    }

    String[] getPostData() throws NoSuchFieldException{

        try {
            String options;
            String[] postData = new String[2];

            while (!(options = this.receiver.readLine()).equals(""))
                this.stringBuilder.append(options).append("\n");

            String[] lines = this.stringBuilder.toString().split("[ \n]+");

            for (int i = 0; i < lines.length; i++)
                if (lines[i].equals("Content-Length:"))
                    postData[0] = lines[i + 1];

            for (int i = 0; i < lines.length; i++)
                if (lines[i].equals("Cookie:"))
                    postData[1] = lines[i + 1];

            return postData;

        }catch (IOException e){
            throw new NoSuchFieldException();
        }
    }

    String getPostBody(String contentLength) throws IOException {
        this.stringBuilder.setLength(0);

        for(int i = 0; i < Integer.parseInt(contentLength); i++)
            stringBuilder.append((char)this.receiver.read());

        return this.stringBuilder.toString();
    }

    void sendCorrectGuessResponse(int guesses, String cookie){
            sendOKResponse(this.html.getCorrectAnswer(guesses), cookie);
    }

    void sendHighGuessResponse(int guesses, String cookie){
            sendOKResponse(this.html.getHighAnswer(guesses), cookie);
    }

    void sendLowGuessResponse(int guesses, String cookie){
            sendOKResponse(this.html.getLowAnswer(guesses), cookie);
    }

    void sendIncorrectInputResponse(int guesses, String cookie){
          sendOKResponse(this.html.getIncorrectInput(guesses), cookie);
    }

    void sendInitialResponse(String cookie){
            sendOKResponse(this.html.getInitialHTML(), cookie);
    }

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
