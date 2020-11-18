/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author fredr
 */
public class UserBean implements Serializable {

    private String name;
    private String email;
    private String password;
    private double average;
    private ArrayList<QuizBean> quizBeans;

    UserBean() {
        this.quizBeans = new ArrayList<>();
        this.average = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public double getAverage() {
        return this.average;
    }

    void addQuiz(QuizBean quiz) {
        this.quizBeans.add(quiz);
        setAverage();
    }

    int getQuizzes() {
        return this.quizBeans.size();
    }

    int getLastQuizPoint() {

        if (this.quizBeans.isEmpty())
            return 0;

        return this.quizBeans.get(this.quizBeans.size() - 1).getQuizPoints();
    }
    
    private void setAverage() {

        double total = 0;

        for (QuizBean quizBean : this.quizBeans)
            total += quizBean.getQuizPoints();

        this.average = total / (double) getQuizzes();
    }
}
