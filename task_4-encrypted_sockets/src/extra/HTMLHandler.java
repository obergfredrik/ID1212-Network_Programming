/**
 * Author: Fredrik Öberg
 * Date of Creation: 201110
 * Date of Latest Update: -
 *
 */

package extra;

/**
 * Contains HTML code used in a game session.
 */
public class HTMLHandler {

    /**
     * Contains HTML code for when a correct guess has been made by the client.
     * @param guesses is the number of guesses made upon this point.
     * @return is the fully prepared HTML code in the form of a String.
     */
    String getCorrectAnswer(int guesses){

        String correct =       "<html>\n" +
                "        <head>\n" +
                "            <title>\n" +
                "                The Guessing Game\n" +
                "            </title>\n" +
                "        </head>\n" +
                "        <body>\n" +
                "            <h3>\n" +
                "                You have made it in " + guesses + " guess(es). Press button to try again\n" +
                "            </h3>\n" +
                "             <form method=\"GET\" action=\"http://localhost:1234\">" +
                "               <input type=\"submit\" value=\"New Game\"/>" +
                "              </form>" +
                "        </body>\n" +
                "</html>";

        return correct;
    }

    /**
     * Contains HTML code for when a high guess has been made by the client.
     * @param guesses is the number of guesses made upon this point.
     * @return is the fully prepared HTML code in the form of a String.
     */
   String getHighAnswer(int guesses){

            String high = "<html>\n" +
                    "        <head>\n" +
                    "            <title>\n" +
                    "                The Guessing Game\n" +
                    "            </title>\n" +
                    "        </head>\n" +
                    "        <body>\n" +
                    "            <h3>\n" +
                    "                Nope, guess lower! You have made " + guesses + " guess(es)  .\n" +
                    "            </h3>\n" +
                    "            <h3>" +
                    "                What's your guess?       " +
                    "            </h3>" +
                    "            <form method=\"POST\">\n" +
                    "              <input name=\"guess\" type=\"text\"/>\n" +
                    "            </form>\n" +
                    "        </body>\n" +
                    "</html>";

            return high;

   }

    /**
     * Contains HTML code for when a low guess has been made by the client.
     * @param guesses is the number of guesses made upon this point.
     * @return is the fully prepared HTML code in the form of a String.
     */
   String getLowAnswer(int guesses){

       String low = "<html>\n" +
               "        <head>\n" +
               "            <title>\n" +
               "                The Guessing Game\n" +
               "            </title>\n" +
               "        </head>\n" +
               "        <body>\n" +
               "            <h3>\n" +
               "                Nope, guess higher! You have made " + guesses + " guess(es)  .\n" +
               "            </h3>\n" +
               "            <h3>" +
               "                What's your guess?       " +
               "            </h3>" +
               "            <form method=\"POST\">\n" +
               "               <input name=\"guess\" type=\"text\"/>\n" +
               "            </form>\n" +
               "        </body>\n" +
               "    </html>";

       return low;
   }

    /**
     * Contains HTML code for when an incorrect input has been made by the client.
     * @param guesses is the number of guesses made upon this point.
     * @return is the fully prepared HTML code in the form of a String.
     */
   String getIncorrectInput(int guesses){

       String low = "<html>\n" +
               "        <head>\n" +
               "            <title>\n" +
               "                The Guessing Game\n" +
               "            </title>\n" +
               "        </head>\n" +
               "        <body>\n" +
               "            <h3>\n" +
               "                You have made an incorrect guess! The input was either not a number or not between 0 or 100. Please try again!" +
               "            </h3>\n" +
               "            <h3>" +
               "              You have made " + guesses + " guess(es)  .\n"+
               "            </h3>" +
               "            <h3>" +
               "                What's your guess?       " +
               "            </h3>" +
               "            <form method=\"POST\">\n" +
               "              <input name=\"guess\" type=\"text\"/>\n" +
               "            </form>\n" +
               "        </body>\n" +
               "    </html>";

       return low;

   }

    /**
     * Contains HTML code for when an initial request without a session cookie has been made by the client.
     * @return is the fully prepared HTML code in the form of a String.
     */
    String getInitialHTML(){

        String initial = "<html>\n" +
                    "        <head>\n" +
                    "            <title>\n" +
                    "                The Guessing Game\n" +
                    "            </title>\n" +
                    "        </head>\n" +
                    "        <body>\n" +
                    "            <h1>\n" +
                    "                Welcome to the number guess game.\n" +
                    "            </h1>\n" +
                    "            <div>\n" +
                    "                <h3>\n" +
                    "                    I'm thinking of a number between 1 and 100. What's your guess?\n" +
                    "                </h3>\n" +
                    "                <form method=\"POST\">\n" +
                    "                   <input name=\"guess\" type=\"text\"/>\n" +
                    "                </form>\n" +
                    "            </div>\n" +
                    "         </body>\n" +
                    "      </html>";

        return initial;
    }

    /**
     * Contains HTML code for when an bad request has been made by the client. Normally
     * when a URL the server does not support has been request via the clients web browser.
     * @return is the fully prepared HTML code in the form of a String.
     */
    String getBadRequestHtml(){

        String html =
                "<html>\n" +
                "        <head>\n" +
                "            <title>\n" +
                "                The Guessing Game\n" +
                "            </title>\n" +
                "        </head>\n" +
                "        <body>\n" +
                "            <h1>\n" +
                "                A page which does not exist was requested.\n" +
                "            </h1>\n" +
                "        </body>\n" +
                "</html>";
        return html;
    }
}
