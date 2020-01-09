/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Entries will clean the ResultSet and prepare the data from the tableViews.
 * @author Dustin
 */
public class Entries {
   
    //Varibles
    private static final ObservableList <Appointments> appointments = FXCollections.observableArrayList();
    private static final ObservableList <Customer> customer = FXCollections.observableArrayList();
    private static final ObservableList <String> correct = FXCollections.observableArrayList();
    
    private static final ObservableList <String> incorrect = FXCollections.observableArrayList();
    
    //Blank Constructor
    public Entries(){}; 
    
    //Appointments List
    public ObservableList getAppointmentList(){        
            return appointments;        
    }
    
    public void addAppointmentEntry(Appointments apt){
        appointments.add(apt);
    }
    
    public void removeAppointmentEntry(int i){
        appointments.remove(i);
    }
    
    //Customer List
    public ObservableList getCustomerList(){
        return customer;
    }
    
    public void addCustomerEntry (Customer cust){
        customer.add(cust);
    }
    
    public void removeCustomerEntry(int i){
        customer.remove(i);
    }
    
    //Correct Log List
    public ObservableList getCorrect(){
        return correct;
    }
    
    public void addCorrect (String string){
        correct.add(string);
    }
    
    public String getCorrectString(int i){
        return correct.get(i);
    }
    
    //Incorrect Log List
    public ObservableList getIncorrect(){
        return incorrect;
    }
    
    public void addIncorrect (String string){
        incorrect.add(string);
    }
    
    public String getIncorrectString(int i){
        return incorrect.get(i);
    }
    
    
    
    
    
  
}
