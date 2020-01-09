/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

//import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author Dustin
 */
public class Appointments {
    
    //Varibles
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String formatStart;
    private String formatEnd;
    private String appointmentType;
    private Date date;
    private String name;
    private int id;
    
    //Blank Constructor
    public Appointments(){};
    //Constructor
    public Appointments(OffsetDateTime startTime, OffsetDateTime endTime, String appointmentType, Date date, String customerName, int id) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentType = appointmentType;
        this.date = date;
        this.name = customerName;
        this.id = id;
    }
    
    //Getters and Setters

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getFormatStart(){
        return formatStart;
    }
    
    public void setFormatStart(OffsetDateTime time){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.formatStart = format.format(time);
    }
    
    public String getFormatEnd(){
        return formatEnd;
    }
    
    public void setFormatEnd(OffsetDateTime time){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.formatEnd = format.format(time);
    }
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
}
