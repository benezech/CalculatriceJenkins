/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author YAR
 */
public class Controler {
    
    private Connection con;
    private String url = "jdbc:mysql://91.134.132.111:3306/";
    private String dbName = "JEGYM";
    private String driver = "com.mysql.jdbc.Driver";
    private String userName = "JEGYM"; 
    private String password = "ZCad0wkeps4kauk6";
    
    public Controler(){
    }
    public void insertQuestion(int idForm, String titreQuestion, String descQuestion){
        try {       
            OpenConnexion();
            Statement st = this.con.createStatement();
            int val = st.executeUpdate("INSERT into DT_QUESTION(ID_FORMULAIRE, TITRE_QUESTION, DESCRIPTION_QUESTION  ) VALUES("+idForm+", '"+titreQuestion+"', '"+descQuestion+"')");
            if(val==1)System.out.print("Question insérée");

            CloseConnexion();
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
    
    public Boolean OpenConnexion(){
        boolean b = true;
        try {
            Class.forName(driver).newInstance();
            this.con = DriverManager.getConnection(url+dbName,userName,password);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }  
        return b;
    }
            
    public Boolean CloseConnexion(){
        boolean b = true;
        try {
            this.con.close();
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }  
        return b;
    }
    
}
