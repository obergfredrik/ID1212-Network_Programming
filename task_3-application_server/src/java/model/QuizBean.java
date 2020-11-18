/*
 * Author: Fredrik Öberg
 * Date of creation: 201118
 *
 */
package model;

import java.io.Serializable;

/**
 * Stores the data of a quiz both in terms of the amount of correct answer and
 * what the user answers were. Follows the Java Bean standard and thereby
 * implements the serializable interface.
 */
public class QuizBean implements Serializable {

    private int quizPoints;
    private String[] userAnswers;

    /**
     * A setter for the user answers.
     *
     * @param userAnswers
     */
    void setUserAnswers(String[] userAnswers) {
        this.userAnswers = userAnswers;
    }

    /**
     * A getter for the user answers.
     *
     * @return is the user answers.
     */
    String[] getUserAnswers() {
        return this.userAnswers;
    }

    /**
     * A setter for the quiz points.
     *
     * @param quizPoints is the points received for the quiz.
     */
    void setQuizPoints(int quizPoints) {
        this.quizPoints = quizPoints;

    }

    /**
     * A getter for the quiz points attribute.
     *
     * @return is the quiz points.
     */
    int getQuizPoints() {
        return this.quizPoints;
    }

}
