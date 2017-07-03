/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Src;


import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author YAR
 */
@Named(value = "form")
@SessionScoped
public class Form implements Serializable {
     private int etape=1;
     private int nbQuestion=1;
     private String question="Veuillez saisir une question";
     private List<String> listReponseEtape=new ArrayList<>();
    /**
     * Creates a new instance of Form
     */
     
     private Controler cont;
     
    public Form() {
        this.cont = new Controler();
    }

    /**
     * @return the etape
     */
    public int getEtape() {
        return etape;
    }

    /**
     * @param etape the etape to set
     */
    public void setEtape(int etape) {
        this.etape = etape;
    }
    public void etapeSuivante(){
        this.ajouterQuestionBdd();
        etape++;
        nbQuestion=1;
    
    }
    public void ajouterQuestion(){
        //transfert dans la bdd
        this.ajouterQuestionBdd();
        // ajouter question 
        nbQuestion++;
        
        
        
        this.cont.insertQuestion(1, "Nouvelle Question", "Question");
        
        
        
        
    }
    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }
    /**
     * Ajoute la question et les réponses dans la bdd
     */
    private void ajouterQuestionBdd(){
        //TODO ajout dans la bdd
        
        setQuestion("Veuillez saisir une nouvelle question");
        
        //TODO vider les collections 
        this.listReponseEtape=new ArrayList<>();
        
    }
    /**
     * Ajoute une nouvelle réponse à partir du dropdown de répnses à la question
     */
    public void ajouterReponse(String s){
        System.out.println("Ajouter réponse");
        //ajouter à collection
        listReponseEtape.add(s);
    }
    /**
     * Supprime une réponse à une question
     * @param idReponse 
     */
    public void supprimerReponse(int idReponse){
        //todo 
    }
    
    /**
     * Supprime une question
     * @param idQuestion 
     */
    public void supprimerQuestion(int idQuestion){
        //penser à supprimer les réponses également
    }

    /**
     * @return the nbQuestion
     */
    public int getNbQuestion() {
        return nbQuestion;
    }

    /**
     * @return the listReponseEtape
     */
    public List<String> getListReponseEtape() {
        return listReponseEtape;
    }

    /**
     * @param listReponseEtape the listReponseEtape to set
     */
    public void setListReponseEtape(List<String> listReponseEtape) {
        this.listReponseEtape = listReponseEtape;
    }
    
}
