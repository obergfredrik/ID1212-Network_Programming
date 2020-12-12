/*
 * Author: Fredrik Öberg
 * Date of creation: 201118
 *
 */
package model;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

/**
 * Handles logic concerning all the users of the WebQuiz application.
 */
public class UserHandler {

    private ArrayList<UserBean> users;

    /**
     * A constructor.
     */
    public UserHandler() {
        this.users = new ArrayList<UserBean>();
        UserBean admin = new UserBean();
        admin.setName("admin");
        admin.setPassword("Password");
        this.users.add(admin);
        UserBean user = new UserBean();
        user.setName("Name");
        user.setPassword("Password");
        this.users.add(user);
    }

    /**
     * Adds a new user if the name choses does not already exist.
     *
     * @param request is the HTTP POST request containing the data needed to
     * create a new user.
     * @return is the response to the user based upon if the registration was
     * successful or not.
     */
    public String addUser(HttpServletRequest request) {

        String name = request.getParameter("name");

        if (getUser(name) != null) {
            return "?response=Name Already Exists!";
        } else {

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            UserBean user = new UserBean();

            user.setName(name);
            user.setPassword(password);
            user.setEmail(email);
            this.users.add(user);

            return "?response=User Created!";

        }
    }

    /**
     * Searsches for a given user in the user list.
     *
     * @param name is the username of interest.
     * @return is the user of interest of null if there is no user of the given
     * name in the database.
     */
    UserBean getUser(String name) {

        for (UserBean userBean : users) {
            if (userBean.getName().equals(name)) {
                return userBean;
            }
        }

        return null;
    }
}
