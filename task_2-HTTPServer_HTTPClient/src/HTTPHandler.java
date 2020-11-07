import java.io.PrintWriter;

public class HTTPHandler {

    private PrintWriter sender;

    public HTTPHandler(PrintWriter sender) {
        this.sender = sender;
    }

    boolean validateGETRequest(String request){


            String[] line = request.split("[ \n\r]+");

            if(3 != line.length)
                return false;

            if (line[0].equals("GET") && line[1].equals("/") && line[2].equals("HTTP/1.1"))
                return true;
            else
                return false;

    }

    boolean validatePOSTRequest(String request){

            String[] line = request.split("[ \n\r]+");

            if(3 != line.length)
                return false;

            if (line[0].equals("POST") && line[1].equals("/") && line[2].equals("HTTP/1.1"))
                return true;
            else
                return false;
    }

    int getContentLength(String options) throws NoSuchFieldException{

            String[] line = options.split("[ \n]+");

            for (int i = 0; i < line.length; i++)
                if (line[i].equals("Content-Length:"))
                    return Integer.parseInt(line[i + 1]);

            throw new NoSuchFieldException();
    }

    int extractGuessValue(String guess) throws NoSuchFieldException{

            String[] value = guess.split("=");

            if (2 != value.length)
                throw new NoSuchFieldException();

            int guessValue = Integer.parseInt(value[1]);

            if(guessValue < 0 || guessValue > 100)
                throw new NumberFormatException();

            return guessValue;
    }

    void sendGuessResponse(String body){

            byte[] responseBuffer = body.getBytes();

            int contentLength = responseBuffer.length;

            this.sender.println("HTTP/1.1 200 OK");
            this.sender.println("Content-Length: " + contentLength);
            this.sender.println("");
            this.sender.println(body);
            this.sender.flush();

    }

    void sendOKResponse(String body){


            byte[] responseBuffer = body.getBytes();
            int contentLength = responseBuffer.length;

            this.sender.println("HTTP/1.1 200 OK");
            this.sender.println("Content-Length: " + contentLength);
            this.sender.println("");
            this.sender.println(body);
            this.sender.flush();

    }

    void sendBadRequestResponse(String body){

            byte[] responseBuffer = body.getBytes();
            int contentLength = responseBuffer.length;

            System.out.println("i bad request vase response sdfsdf");

            this.sender.println("HTTP/1.1 400 Bad Request");
            this.sender.println("Content-Length: " + contentLength);
            this.sender.println("");
            this.sender.println(body);
            this.sender.flush();
    }
}
