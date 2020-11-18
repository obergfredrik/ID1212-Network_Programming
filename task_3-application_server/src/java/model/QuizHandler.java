/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fredr
 */
public class QuizHandler {
    
       private String[] correctAnswers = { "a", "b", "d", "b" };
       
       
       boolean handleQuiz(HttpServletRequest request, UserBean user){
       
           String[] userAnswers = extractAnswers(request);
           
           if(checkAnswers(userAnswers)){
               QuizBean quiz = new QuizBean(); 
               quiz.setUserAnswers(userAnswers);
               setQuizPoints(userAnswers, quiz);               
               user.addQuiz(quiz);
               return true;
           }else
               return false;
       
       }
       
       private void setQuizPoints(String[] userAnswers, QuizBean quiz)  {
           
           int quizPoints = 0;

           for (int i = 0; i < userAnswers.length; i++)
              if (userAnswers[i].equals(this.correctAnswers[i]))
                  quizPoints++;
            
            quiz.setQuizPoints(quizPoints);         
       }
       
      private String[] extractAnswers(HttpServletRequest request){
           
           String[] userAnswers = new String[this.correctAnswers.length];
       
           for(int i = 1; i <= this.correctAnswers.length; i++)
               userAnswers[i - 1] =  request.getParameter("q" + i);
       
           
           return userAnswers;
       }
       
       boolean checkAnswers(String[] userAnswers) {

            for (String string : userAnswers)
                if (string == null)
                    return false;

            return true;
        }
}
