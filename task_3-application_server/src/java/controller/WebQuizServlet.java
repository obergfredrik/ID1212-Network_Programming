/*
 * Author: Fredrik Öberg
 * Date of creation: 201118
 *
 */
package controller;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import model.SessionHandler;
import model.UserBean;
import model.UserHandler;

/**
 * The class extends the servlet class and works as a controller for the WebQuiz
 * application.
 *
 */
public class WebQuizServlet extends HttpServlet {

    private RequestDispatcher dispatcher;
    private HttpSession session;
    private UserHandler userHandler = new UserHandler();
    private UserBean user;
    private SessionHandler sessionHandler = new SessionHandler(this.userHandler);

    /**
     * Called upon when the application receives HTTP GET requests.
     *
     * @param request is the request from the client.
     * @param response is the response to the client
     * @throws ServletException is thrown if the servlet encounters difficulty
     * performing its tasks.
     * @throws IOException is thrown an I/O there has been some errors
     * concerning input or output.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        handleRequest(request);

        if (this.user != null) {
            this.dispatcher = request.getRequestDispatcher("userPage.jsp" + this.sessionHandler.getUserSession(this.user));
        } else {
            this.dispatcher = request.getRequestDispatcher("login.jsp");
        }

        this.dispatcher.forward(request, response);
    }

    /**
     * Called upon when the application receives HTTP POST requests and handles
     * those requests.
     *
     * @param request is the request from the client.
     * @param response is the response to the client
     * @throws ServletException is thrown if the servlet encounters difficulty
     * performing its tasks.
     * @throws IOException is thrown an I/O there has been some errors
     * concerning input or output.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        handleRequest(request);

        if (this.user == null) {

            if (request.getParameter("toregister") != (null)) {
                this.dispatcher = request.getRequestDispatcher("register.jsp");
            } else if (request.getParameter("register") != null) {
                this.dispatcher = request.getRequestDispatcher("register.jsp" + this.userHandler.addUser(request));
            } else if (request.getParameter("login") != null) {
                this.dispatcher = request.getRequestDispatcher(this.sessionHandler.loginRequest(request, this.session, "userPage.jsp", "login.jsp"));
            } else {
                this.dispatcher = request.getRequestDispatcher("login.jsp");
            }

        } else {

            if (request.getParameter("logout") != null) {
                this.sessionHandler.logoutRequest(this.session);
                this.dispatcher = request.getRequestDispatcher("login.jsp");
            } else if (request.getParameter("newquiz") != null) {
                this.dispatcher = request.getRequestDispatcher("quizForm.jsp");
            } else {
                this.dispatcher = request
                        .getRequestDispatcher(this.sessionHandler.newQuizSubmit(request, this.session, "userPage.jsp", "quizForm.jsp"));
            }

        }

        this.dispatcher.forward(request, response);
    }

    /**
     * Extracts the session from the received request and sets the sessions
     * maximum life expectancy until next request. Also extracts the logged in
     * user of the session which becomes null if the user is not logged in.
     *
     * @param request is the client request to the server.
     */
    void handleRequest(HttpServletRequest request) {

        this.session = request.getSession(true);
        this.session.setMaxInactiveInterval(300);
        this.user = (UserBean) this.session.getAttribute("user");
    }

}
