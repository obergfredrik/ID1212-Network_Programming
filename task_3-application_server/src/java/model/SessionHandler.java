/*
 * Author: Fredrik Öberg
 * Date of creation: 201118
 *
 */
package model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Handles the logic behind each HTTP session in the WebQuiz application.
 */
public class SessionHandler {

    private UserHandler users;
    private QuizHandler quizHandler;
    private QuestionHandler questionHandler;

    /**
     * A constructor.
     *
     * @param users is the users registered in the WebQuiz database.
     */
    public SessionHandler(UserHandler users) {
        this.users = users;
        this.quizHandler = new QuizHandler();
        this.questionHandler = new QuestionHandler();
        this.quizHandler.updateQuiz(questionHandler.getQuestions());
    }

    /**
     * Handles login request sent by the client.
     *
     * @param request is the POST request received from the client.
     * @param session is the current session.
     * @param correct is the page the client is sent to if the login is a
     * success.
     * @param incorrect is the page the client is sent to the login is a
     * faliure.
     * @return is the response being sent to the client and contains the
     * appropriate session data in the form of a String and is HTTP compatible.
     */
    public String loginRequest(HttpServletRequest request, HttpSession session, String correct, String incorrect, String admin) {

        UserBean user;
        String password;

        if ((user = this.users.getUser(request.getParameter("name"))) != null) {
            if ((password = user.getPassword()).equals(request.getParameter("password"))) {
                session.setAttribute("user", user);
                
                if(user.getName().equals("admin"))
                    return admin;
                else
                    return correct + getUserSession(user);
            }
        }

        return incorrect + "?response=Invalid name and/or password!";

    }

    /**
     * Extracts and returns the user data of the current session.
     *
     * @param user is the user of the current session.
     * @return is the user data in the form of a String and HTTP compatible.
     */
    public String getUserSession(UserBean user) {
        return "?quizzes=" + user.getQuizzes() + "&average=" + user.getAverage() + "&name=" + user.getName()
                + "&answer=" + user.getLastQuizPoint() + "&questions=" + user.getLastQuizSize();
    }

    /**
     * Handles the case of when a new quiz has been submitted by a user.
     *
     * @param request contains the quiz data in the form of a HTTP POST request.
     * @param session is the current user session
     * @param correct is the page the client is sent to if the quiz has been
     * submitted correctly.
     * @param incorrect is the page the client is sent to if the quiz has been
     * submitted incorrectly.
     * @return is the page as well as HTTP data in the form os a String.
     */
    public String newQuizSubmit(HttpServletRequest request, HttpSession session, String correct, String incorrect) {

        UserBean user = (UserBean) session.getAttribute("user");

        if (this.quizHandler.handleQuiz(request, user)) {
            return correct + getUserSession(user);
        } else {
            return incorrect + "?response=You have missed to check one or more answers!&quiz=" + this.quizHandler.getQuiz();
        }

    }

    /**
     * Logs the current user out by removing the attribute "user" from the
     * current session.
     *
     * @param session is the current user session.
     */
    public void logoutRequest(HttpSession session) {
        session.removeAttribute("user");
    }
    
    public String newQuestion(HttpServletRequest request){
        
       String response =  this.questionHandler.addQuestion(request);
       
       if(response.equals("Question was added to database!"))
           this.quizHandler.updateQuiz(questionHandler.getQuestions());
        
       return "?response=" + response;
            
    }   

    public String generateQuiz(){
        return "?quiz=" + this.quizHandler.getQuiz();
    }
}
