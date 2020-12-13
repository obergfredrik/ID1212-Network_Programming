/*
 * Author: Fredrik Öberg
 * Date of creation: 201213
 *
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A question for the quiz containing the it id for the primary key in the database, the question attribute 
 * as well as four different answerr choices and the correct choice. The class implements serializable so that in can
 * use the Java Persistence API to persist the object in a database.
 */
@Entity
@Table(name = "question")
@NamedQueries({
    @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q"),
    @NamedQuery(name = "Question.findById", query = "SELECT q FROM Question q WHERE q.id = :id"),
    @NamedQuery(name = "Question.findByA", query = "SELECT q FROM Question q WHERE q.a = :a"),
    @NamedQuery(name = "Question.findByB", query = "SELECT q FROM Question q WHERE q.b = :b"),
    @NamedQuery(name = "Question.findByC", query = "SELECT q FROM Question q WHERE q.c = :c"),
    @NamedQuery(name = "Question.findByCorrect", query = "SELECT q FROM Question q WHERE q.correct = :correct"),
    @NamedQuery(name = "Question.findByD", query = "SELECT q FROM Question q WHERE q.d = :d"),
    @NamedQuery(name = "Question.findByQuestion", query = "SELECT q FROM Question q WHERE q.question = :question")})
public class Question implements Serializable {

    /**
     * Class attributes.
     */
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "a")
    private String a;
    @Column(name = "b")
    private String b;
    @Column(name = "c")
    private String c;
    @Column(name = "correct")
    private String correct;
    @Column(name = "d")
    private String d;
    @Column(name = "question")
    private String question;
    
    /**
     * A constructor.
     */
    public Question() {
    }

    /**
     * A constructor.
     * 
     * @param id is the primary ley attribute of the class. 
     */
    public Question(Integer id) {
        this.id = id;
    }

    /**
     * A getter.
     * 
     * @return is the class id. 
     */
    public Integer getId() {
        return id;
    }

    /**
     * An id setter.
     * 
     * @param id is the new id value. 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * A getter for the attribute a.
     * 
     * @return is the value of the class attribute a. 
     */
    public String getA() {
        return a;
    }

    /**
     * A setter for the class attribute a.
     * 
     * @param a is the new value of a.
     */
    public void setA(String a) {
        this.a = a;
    }

     /**
     * A getter for the attribute b.
     * 
     * @return is the value of the class attribute b. 
     */
    public String getB() {
        return b;
    }

        /**
     * A setter for the class attribute b.
     * 
     * @param a is the new value of b.
     */
    public void setB(String b) {
        this.b = b;
    }

     /**
     * A getter for the attribute c.
     * 
     * @return is the value of the class attribute c. 
     */
    public String getC() {
        return c;
    }

        /**
     * A setter for the class attribute c.
     * 
     * @param a is the new value of c.
     */
    public void setC(String c) {
        this.c = c;
    }

     /**
     * A getter for the attribute correct.
     * 
     * @return is the value of the class attribute correct. 
     */
    public String getCorrect() {
        return correct;
    }

     /**
     * A setter for the class attribute correct.
     * 
     * @param a is the new value of correct.
     */
    public void setCorrect(String correct) {
        this.correct = correct;
    }

     /**
     * A getter for the attribute d.
     * 
     * @return is the value of the class attribute d. 
     */
    public String getD() {
        return d;
    }

    /**
     * A setter for the class attribute d.
     * 
     * @param a is the new value of d.
     */
    public void setD(String d) {
        this.d = d;
    }

    
     /**
     * A getter for the attribute question.
     * 
     * @return is the value of the class attribute question. 
     */
    public String getQuestion() {
        return question;
    }

    /**
     * A setter for the class attribute question.
     * 
     * @param a is the new value of question.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Creates a hashcode for the primary key id.
     * 
     * @return is the hash value of the id attribute. 
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Checks if the class instance is equal to a given object.
     * 
     * @param object is the object being compared to the class instance.
     * @return is true if the class instance and the object parameters are equal. False if not.
     */
    @Override
    public boolean equals(Object object) {
     
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Creates a string of the class instance.
     * 
     * @return is the class instance in the form of a string. 
     */
    @Override
    public String toString() {
        return "model.Question[ id=" + id + " ]";
    }
    
}
