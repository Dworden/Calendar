/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import static Model.DBConnection.con;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Dustin
 */
public class Query {
    
    private static String query;
    private static Statement stmt;
    private static ResultSet results;
    
    
    
    public static void makeQuery(String q){
        
        query = q;
        
        try{
		//create Statement object
		stmt = con.createStatement();
		

		//Determine query execution
		if(query.toLowerCase().startsWith("select")){results=stmt.executeQuery(query);}

		if(query.toLowerCase().startsWith("delete")  ||  query.toLowerCase().startsWith("insert")  ||  query.toLowerCase().startsWith("update")){stmt.executeUpdate(query);}
                } catch(Exception ex){

			System.out.println("Error: "+ ex);
                    }
    
    }
    
    
    public static ResultSet getResults(){
        return results;
    }
    
}


