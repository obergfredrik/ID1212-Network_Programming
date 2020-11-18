package model;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

public class UserHandler {
    private ArrayList<UserBean> users;

    public UserHandler() {
        this.users = new ArrayList<UserBean>();
    }

    public String addUser(HttpServletRequest request) {

        String name = request.getParameter("name");
        
        if (getUser(name) != null)
            return "?response=Name Already Exists!";
        else {
            
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

    UserBean getUser(String name) {

        for (UserBean userBean : users)
            if (userBean.getName().equals(name))
                return userBean;

        return null;
    }
}
