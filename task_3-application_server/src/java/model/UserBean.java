/*
 * Author: Fredrik Öberg
 * Date of creation: 201118
 *
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Stores the data of a registered user. Follows the Java Bean standard and
 * thereby implements the serializable interface.
 */
public class UserBean implements Serializable {

    private String name;
    private String email;
    private String password;
    private double average;
    private ArrayList<QuizBean> quizBeans;

    /**
     * A constructor.
     */
    UserBean() {
        this.quizBeans = new ArrayList<>();
        this.average = 0;
    }

    /**
     * A setter for the name attribute.
     *
     * @param name is the name being set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A setter for the email attribute.
     *
     * @param email is the email being set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Is a setter for the password attribute.
     *
     * @param password is the password being set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Is a getter for the name attribute.
     *
     * @return is the name attribute
     */
    public String getName() {
        return this.name;
    }

    /**
     * Is a getter for the email attribute.
     *
     * @return is the email attribute
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Is a getter for the password attribute.
     *
     * @return is the password attribute
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Is a getter for the average attribute.
     *
     * @return is the average attribute
     */
    public double getAverage() {
        return this.average;
    }

    /**
     * Adds a new quiz to the user attribute.
     *
     * @param quiz is the quiz being added.
     */
    void addQuiz(QuizBean quiz) {
        this.quizBeans.add(quiz);
        setAverage();
    }

    /**
     * Is a getter for the number of quizzes the user has performed.
     *
     * @return is the total number of quizzes.
     */
    int getQuizzes() {
        return this.quizBeans.size();
    }

    /**
     * Is a getter for the latest quiz the user has performed.
     *
     * @return is the points of the latest quiz the user has performed.
     */
    int getLastQuizPoint() {

        if (this.quizBeans.isEmpty()) {
            return 0;
        }

        return this.quizBeans.get(this.quizBeans.size() - 1).getQuizPoints();
    }

    /**
     * Sets the average quiz points of the users total number of quizzes.
     */
    private void setAverage() {

        double total = 0;

        for (QuizBean quizBean : this.quizBeans) {
            total += quizBean.getQuizPoints();
        }

        this.average = total / (double) getQuizzes();
    }
    
    public int getLastQuizSize(){
        
        if(this.quizBeans.size() == 0)
            return 0;
        else
            return this.quizBeans.get(this.quizBeans.size() - 1).getQuizSize();
    }
}
