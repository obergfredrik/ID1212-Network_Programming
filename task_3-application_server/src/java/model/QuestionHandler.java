/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fredr
 */
public class QuestionHandler {
    
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("task_3-application_serverPU");
    private final int submissionSize = 6;
    
    
     List<Question> getQuestions() {
        
    	EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
    	
    	String strQuery = "SELECT q FROM Question q WHERE q.id IS NOT NULL";
    	
    	TypedQuery<Question> tq = em.createQuery(strQuery, Question.class);
    	List<Question> questions = null;
        
    	try {    		
    		questions = tq.getResultList();
                
    		//questions.forEach(question->System.out.println(question.getQuestion() + " " + question.getCorrect()));
    	}
    	catch(NoResultException ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		em.close();
                return questions;
    	}
    }
    
     public String addQuestion(HttpServletRequest request){
         
        String[] attributes = extractQuestion(request);
        
        if(!checkQuestion(attributes))
            return "You have missed to add one or more of the question attributes";
        
        if(sendQuestionToDatabase(createQuestion(attributes)))                   
           return "Question was added to database!";
        else
            return "Some error occured when connecting to database. Question was not added!";
 
        
    }
     
    private Question createQuestion(String[] attributes){
            
        Question question = new Question();
        
        question.setQuestion(attributes[0]);
        question.setA(attributes[1]);
        question.setB(attributes[2]);
        question.setC(attributes[3]);
        question.setD(attributes[4]);
        question.setCorrect(attributes[5]);
        
        return question;
    } 
     
    private boolean sendQuestionToDatabase(Question question){
    
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();       
        EntityTransaction entityTransaction = null;
                
        try{
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();            
            entityManager.persist(question);
            entityTransaction.commit();
        }catch(Exception e){
            
            if(entityTransaction != null)
                entityTransaction.rollback();
            
            e.printStackTrace();
            return false;
        }finally{
            
            entityManager.close();
            return true;
        }
    } 
    
    private String[] extractQuestion(HttpServletRequest request){
    
        String[] values = new String[submissionSize];
        
        values[0] = request.getParameter("question");
        
        for(int i = 1;  i < 5; i++)
            values[i] = request.getParameter("c" + i);
        
        values[5] = request.getParameter("correct");
        
        return values;
    }
    
    private boolean checkQuestion(String[] question){
    
         for(int i = 0; i < submissionSize - 1; i++)
             if(question[i].isEmpty())
                 return false;
         
         if(question[submissionSize - 1] == null)
             return false;
         else
             return true; 
    
    }
    
    
}
