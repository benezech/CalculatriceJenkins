package Src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;


@ManagedBean
public class SelectOneMenuView {
     
   
    private String rep;  
    private List<SelectItem> questions;
 
  

    @PostConstruct
    public void init() {
        SelectItemGroup g1 = new SelectItemGroup("Réponse texte");
        g1.setSelectItems(new SelectItem[] {new SelectItem("1", "Texte"), new SelectItem("2", "Texte Libre")});
         
        SelectItemGroup g2 = new SelectItemGroup("Réponse Nombre");
        g2.setSelectItems(new SelectItem[] {new SelectItem("10", "Entier"), new SelectItem("11", "Décimal")});
        
        SelectItemGroup g3 = new SelectItemGroup("Spécial");
        g3.setSelectItems(new SelectItem[] {new SelectItem("20", "Date")});
        
        questions = new ArrayList<SelectItem>();
        questions.add(g1);
        questions.add(g2);
        questions.add(g3); 

    }
 
 
    public String getRep() {
        return rep;
    }
 
    public void setRep(String rep) {
        this.rep = rep;
    }
 

    public List<SelectItem> getQuestions() {
        return questions;
    }
 
 

}