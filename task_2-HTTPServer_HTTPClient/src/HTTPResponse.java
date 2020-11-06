import java.io.PrintWriter;

public class HTTPResponse {

    private String CRLF = "\n\r";

    void HTTPOk(PrintWriter sender, String body){


            byte[] toClientBuffer;
            int contentLength;


            toClientBuffer = body.getBytes();
            contentLength = toClientBuffer.length;

            sender.println("HTTP/1.1 200 OK");
            sender.println("Content-Length: " + contentLength);
            sender.println("Content-Type: text/plain; charset=utf-8");
            sender.println("Connection: Closed");
            sender.println("");
            sender.println(body);
            sender.flush();








      /*  String response = "HTTP/1.1 200 OK" + this.CRLF
                        + "Content-Length: " + body.getBytes().length + this.CRLF
                        + "Content-Type: text/plain; charset=utf-8" + this.CRLF
                        + this.CRLF
                        + body
                        + this.CRLF + this.CRLF;

        return response;*/
    }

}
