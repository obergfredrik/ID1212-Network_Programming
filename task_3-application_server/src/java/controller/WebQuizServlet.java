
package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import model.SessionHandler;
import model.UserBean;
import model.UserHandler;

public class WebQuizServlet extends HttpServlet {

    private RequestDispatcher dispatcher;
    private HttpSession session;
    private UserHandler userHandler = new UserHandler();
    private UserBean user;
    private SessionHandler sessionHandler = new SessionHandler(this.userHandler);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
   
        handleRequest(request);

        if (this.user != null)
            this.dispatcher = request.getRequestDispatcher("userPage.jsp" + this.sessionHandler.getUserSession(this.user));
        else
            this.dispatcher = request.getRequestDispatcher("login.jsp");

        this.dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        handleRequest(request);

        if (this.user == null) {
            
            if (request.getParameter("toregister") != (null))
                this.dispatcher = request.getRequestDispatcher("register.jsp");
            else if (request.getParameter("register") != null)
                this.dispatcher = request.getRequestDispatcher("register.jsp" + this.userHandler.addUser(request));
            else if(request.getParameter("login") != null)  
                this.dispatcher = request.getRequestDispatcher(this.sessionHandler.loginRequest(request , this.session, "userPage.jsp", "login.jsp"));  
            else
                 this.dispatcher = request.getRequestDispatcher("login.jsp");
                   
        } else {

            if (request.getParameter("logout") != null) {
                this.sessionHandler.closeSession(this.session);
                this.dispatcher = request.getRequestDispatcher("login.jsp");
            } else if (request.getParameter("newquiz") != null)
                this.dispatcher = request.getRequestDispatcher("quizForm.jsp");
            else
                this.dispatcher = request
                        .getRequestDispatcher(this.sessionHandler.newQuizSubmit(request, this.session, "userPage.jsp", "quizForm.jsp"));

        }

        this.dispatcher.forward(request, response);
    }
    
    
    void handleRequest(HttpServletRequest request){
        
        this.session = request.getSession(true);
        this.session.setMaxInactiveInterval(300);
        this.user = (UserBean) this.session.getAttribute("user");
    }
    
}
