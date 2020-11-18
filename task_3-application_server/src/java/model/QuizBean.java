/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author fredr
 */
public class QuizBean implements Serializable{

    private int quizPoints;
    private String[] userAnswers;

    void setUserAnswers(String[] userAnswers) {
        this.userAnswers = userAnswers;
    }
    
    String[] getUserAnswers() {
            return this.userAnswers;
    }

    void setQuizPoints(int quizPoints){
        this.quizPoints = quizPoints;
  
    }

    int getQuizPoints() {
        return this.quizPoints;
    }

}
