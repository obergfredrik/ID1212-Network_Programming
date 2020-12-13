/*
 * Author: Fredrik Öberg
 * Date of creation: 201213
 *
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
 * Handles all question related procedures of the WebQuiz system.
 */
public class QuestionHandler {
    
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("task_3-application_serverPU");
    private final int submissionSize = 6;
    
    /**
     * Connects to a SQL database and retreives the questions stored in it.
     * 
     * @return is the questions stored in the database in the form of a list.
     */
     List<Question> getQuestions() {
        
    	EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
    	
    	String strQuery = "SELECT q FROM Question q WHERE q.id IS NOT NULL";
    	
    	TypedQuery<Question> tq = em.createQuery(strQuery, Question.class);
    	List<Question> questions = null;
        
    	try {    		
    		questions = tq.getResultList();               
    	}
    	catch(NoResultException ex) {
    		ex.printStackTrace();
    	}
    	finally {
    		em.close();
                return questions;
    	}
    }
    
     /**
      * Sends a question added by the system admin to the connected database.
      * 
      * @param request is the POST request containing all the entered information concerning the new question.
      * @return is true if the question could be added to the database. False if not.
      */
     public String addQuestion(HttpServletRequest request){
         
        String[] attributes = extractQuestion(request);
        
        if(!checkQuestion(attributes))
            return "You have missed to add one or more of the question attributes or the length of the attribute is to long!";
        
        if(sendQuestionToDatabase(createQuestion(attributes)))                   
           return "Question was added to database!";
        else
            return "Some error occured when connecting to database. Question was not added!";
 
        
    }
     
     /**
      * Creates a new question from the admin entered attributes.
      * 
      * @param attributes is the attributes of the new question in the form of an array of strings.
      * @return is the created question object.
      */
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
     
    /**
     * Sende the created question to the database.
     * 
     * @param question it the question being sent.
     * @return is true if the question could be sent and stored. False if not.
     */
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
    
    /**
     * Extracts the question attributes from the POST request sent by the admin.
     * 
     * @param request is the request containing the question attributes.
     * @return is the attributes in teh form of an array of strings.
     */
    private String[] extractQuestion(HttpServletRequest request){
    
        String[] values = new String[submissionSize];
        
        values[0] = request.getParameter("question");
        
        for(int i = 1;  i < 5; i++)
            values[i] = request.getParameter("c" + i);
        
        values[5] = request.getParameter("correct");
        
        return values;
    }
    
    /**
     * Checks to see if the extracted question attributes is empty of longer than 
     * 255 characters which is the maximum size stored in the database.
     * 
     * @param question is the question attributes.
     * @return is true if the attributes follows the established convention. False if not.
     */
    private boolean checkQuestion(String[] question){
    
         for(int i = 0; i < submissionSize - 1; i++)
             if(question[i].isEmpty() || question[i].length() > 255)
                 return false;
         
         if(question[submissionSize - 1] == null)
             return false;
         else
             return true; 
    
    }
    
    
}
