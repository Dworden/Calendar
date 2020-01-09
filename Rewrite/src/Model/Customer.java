/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Dustin
 */
public class Customer {

    //Varibles.
    private int customerID;
    private int addressID;
    private int cityID;
    private int countryID;
    private String name;
    private String address_1;
    private String address_2;
    private String city;
    private String country;
    private String zip;
    private String phone;
    private ObservableList <Appointments> appointmentList = FXCollections.observableArrayList();
    
    
    
    
    //Blank Constructor
    public Customer(){};
    //Constructor
    public Customer(int ID, String name,String ad_1, String ad_2, String city,
            String zip, String phone, String country, int addressID, int cityID, int countryID){
        
        this.customerID = ID;
        this.name = name;
        this.address_1 = ad_1;
        this.address_2 = ad_2;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.country = country;
        this.addressID = addressID;
        this.cityID = cityID;
        this.countryID = countryID;
        this.appointmentList = FXCollections.observableArrayList();
        
    };
    
    
    
    //Getters and Setters

    public int getID(){
        return customerID;
    }
    
    public void setID(int id){
        this.customerID = id;
    }
    
    public int getAddressID(){
        return addressID;
    }
    
    public void setAddressID(int id){
        this.addressID = id;
    }
    
    public int getCityID(){
        return cityID;
    }
    
    public void setCityID(int id){
        this.cityID = id;
    }
    
    public int getCountryID(){
        return countryID;
    }
    
    public void setCountryID(int id){
        this.countryID = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public ObservableList getAppointmentList(){
        return appointmentList;
    }
    
    public Appointments getAppointment(int i){
        return appointmentList.get(i);
    }
    
    public void removeAppointment(int i){
        appointmentList.remove(i);
    }
    
    public void addAppointment(Appointments apt){
        appointmentList.add(apt);
    }
    
    
}    