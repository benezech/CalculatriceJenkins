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
    
    Connection con;
    
    public Controler(){
    String url = "jdbc:mysql://91.134.132.111:3306/";
		  String dbName = "JEGYM";
		  String driver = "com.mysql.jdbc.Driver";
		  String userName = "JEGYM"; 
		  String password = "ZCad0wkeps4kauk6";
		  try {
		  Class.forName(driver).newInstance();
		  this.con = DriverManager.getConnection(url+dbName,userName,password);
		  
                  
                  Statement st = this.con.createStatement();
		  ResultSet res = st.executeQuery("SELECT * FROM  DT_QUESTION");
		  while (res.next()) {
		  int id = res.getInt("ID_QUESTION");
		  String msg = res.getString("TITRE_QUESTION");
		  System.out.println(id + "\t" + msg);
		  }
                  int val = st.executeUpdate("INSERT into DT_QUESTION(ID_FORMULAIRE, TITRE_QUESTION, DESCRIPTION_QUESTION  ) VALUES(1, 'Un titre', 'Une descr')");
		  if(val==1)
			  System.out.print("Successfully inserted value");
                  
                  this.con.close();
		  } catch (Exception e) {
		  e.printStackTrace();
		  }           
    }
    public void insertQuestion(int idForm, String titreQuestion, String descQuestion){
       /* try {                  
                  Statement st = this.con.createStatement();
                  int val = st.executeUpdate("INSERT into DT_QUESTION(ID_FORMULAIRE, TITRE_QUESTION, DESCRIPTION_QUESTION  ) VALUES("+idForm+", '"+titreQuestion+"', '"+descQuestion+"')");
		  if(val==1)System.out.print("Question insérée");
                  
                  this.con.close();
		  } catch (Exception e) {
		  e.printStackTrace();
		  }   */
    }
    
    
}
