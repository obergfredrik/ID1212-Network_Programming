/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author fredr
 */
public class SessionHandler {

    private UserHandler users;
    private QuizHandler quizHandler;

    public SessionHandler(UserHandler users) {
        this.users = users;
        this.quizHandler = new QuizHandler();
    }

    public String loginRequest(HttpServletRequest request, HttpSession session, String correct, String incorrect) {

        UserBean user;
        String password;

        if ((user = this.users.getUser(request.getParameter("name"))) != null)
            if ((password = user.getPassword()).equals(request.getParameter("password"))) {
                session.setAttribute("user", user);
                return correct + getUserSession(user);
            }
        
        return incorrect + "?response=Invalid name and/or password!";
        
    }

    public String getUserSession(UserBean user) {
        return "?quizzes=" + user.getQuizzes() + "&average=" + user.getAverage() + "&name=" + user.getName()
                + "&answer=" + user.getLastQuizPoint();
    }

    public String newQuizSubmit(HttpServletRequest request, HttpSession session, String correct, String incorrect) {

        UserBean user = (UserBean) session.getAttribute("user");
        
        if(this.quizHandler.handleQuiz(request, user))
            return correct + getUserSession(user);
        else
            return incorrect + "?response=You have missed to check one or more answers!";

       
    }
    
    public void closeSession(HttpSession session){
        session.removeAttribute("user");
    }
}
