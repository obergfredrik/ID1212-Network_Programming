public class HTMLHandler {

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
                "            <button type=\"button\">" +
                "                New Game" +
                "            </button>" +
                "        </body>\n" +
                "</html>";

        return correct;
    }

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
