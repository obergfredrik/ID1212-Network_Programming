/*
 * Author: Fredrik Öberg
 * Date of creation: 201118
 *
 */
package model;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles the quiz answers entered by a user of the WebQuiz application.
 *
 */
public class QuizHandler {

    private String[] correctAnswers = {"a", "b", "d", "b"};

    /**
     * Handles the quiz answers entered with the help of some helper methods.
     *
     * @param request is the POST request from the client and contains the users
     * quiz choices.
     * @param user is the user who has performed the quiz.
     * @return is true if the quiz was correctly handled and false if not.
     */
    boolean handleQuiz(HttpServletRequest request, UserBean user) {

        String[] userAnswers = extractAnswers(request);

        if (checkAnswers(userAnswers)) {
            QuizBean quiz = new QuizBean();
            quiz.setUserAnswers(userAnswers);
            setQuizPoints(userAnswers, quiz);
            user.addQuiz(quiz);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Sets the quiz point of the current quiz.
     *
     * @param userAnswers is the clients quiz answers.
     * @param quiz is the current quiz.
     */
    private void setQuizPoints(String[] userAnswers, QuizBean quiz) {

        int quizPoints = 0;

        for (int i = 0; i < userAnswers.length; i++) {
            if (userAnswers[i].equals(this.correctAnswers[i])) {
                quizPoints++;
            }
        }

        quiz.setQuizPoints(quizPoints);
    }

    /**
     * Extracts the answers from the POST request.
     *
     * @param request is the POST request containing the quiz answers.
     * @return is the users answers in the form of a String array.
     */
    private String[] extractAnswers(HttpServletRequest request) {

        String[] userAnswers = new String[this.correctAnswers.length];

        for (int i = 1; i <= this.correctAnswers.length; i++) {
            userAnswers[i - 1] = request.getParameter("q" + i);
        }

        return userAnswers;
    }

    /**
     * Checks if all the questions of the quiz has had a corect input.
     *
     * @param userAnswers is the answers entered by the current user.
     *
     * @return is true if all questions where answered. False if not.
     */
    boolean checkAnswers(String[] userAnswers) {

        for (String string : userAnswers) {
            if (string == null) {
                return false;
            }
        }

        return true;
    }
}
