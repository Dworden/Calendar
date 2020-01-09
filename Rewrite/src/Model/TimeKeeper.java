/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Dustin
 */
public class TimeKeeper {
    
    private final String fileName = "User_Log.txt";
    private final String errorFile = "Failed_Log.txt";
    private final String workingDirectory = System.getProperty("user.dir");
    
    
    public TimeKeeper(){};
    
    
    //Logs Current Time on successful Log in.
    public void Log() throws IOException{
        
        //File writer/varibles.
        File file = new File(workingDirectory, fileName);
        FileWriter fileWriter = new FileWriter(file, true);
        
        //Get current date.
        java.util.Date currentDate = Calendar.getInstance(TimeZone.getDefault()).getTime(); 
      
        //Try-catch block,check file creation, write current date log, close file
        //<editor-fold>
        try {
    		 
	      
	      
	      if (file.createNewFile()){
                fileWriter.write(currentDate.toString() + System.lineSeparator());
                fileWriter.close();
	      }else{
                fileWriter.write(currentDate.toString() + System.lineSeparator());
                fileWriter.close();
	      }
	      
    	} catch (IOException e) {
	}
        //</editor-fold>
        
    }
    
    
    public void LogError() throws IOException{
        
        //File writer/varibles.
        File file = new File(workingDirectory, errorFile);
        FileWriter fileWriter = new FileWriter(file, true);
        
        //Get current date.
        java.util.Date currentDate = Calendar.getInstance(TimeZone.getDefault()).getTime(); 
      
        //Try-catch block,check file creation, write current date log, close file
        //<editor-fold>
        try {
    		 
	      
	      
	      if (file.createNewFile()){
	        
                fileWriter.write(currentDate.toString() + System.lineSeparator());
                fileWriter.close();
	      }else{
	        
                fileWriter.write(currentDate.toString() + System.lineSeparator());
                fileWriter.close();
	      }
	      
    	} catch (IOException e) {
	}
        //</editor-fold>
    }
    public Boolean withinTime(OffsetDateTime start){
        
        Boolean inFifteen = false;
        OffsetDateTime now = OffsetDateTime.now();
        
        if(start.isAfter(now) && start.isBefore(now.plusMinutes(15))){
            
            inFifteen = true;
        }
        return inFifteen;
    }
    
    public OffsetDateTime convertToUTC(OffsetDateTime offset) {
        
        
        
        ZonedDateTime zdt = offset.toZonedDateTime();
        OffsetDateTime utc = zdt.withZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime();
        
       
  
        return utc;
    }
    
    public OffsetDateTime convertToLocale(Time utc, Date date) throws ParseException {
        
        String utcTime = utc.toString();
        String utcDate = date.toString();
        String dateTime = utcDate + "T" + utcTime;
        LocalDateTime localTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        Instant instant = Instant.now();
        ZoneId zone = ZoneId.systemDefault();
        ZoneOffset offset = zone.getRules().getOffset(instant);
        OffsetDateTime utcOffset = localTime.atOffset(ZoneOffset.UTC);
        OffsetDateTime offSetTime = utcOffset.withOffsetSameInstant(offset);
        
       
        return offSetTime;
        
    }
    
    public Boolean overLapAppointments (String start, String end, Date date){
        
        
        
        Boolean hasOverLap = false;
        ResultSet results;
        try {
                DBConnection.makeConnection();
                String q = "SELECT appointmentId,start,end FROM U05EJ7.appointment \n"
                           +"WHERE(SELECT date(start) = '" + date + "');";
               
               
                Query.makeQuery(q);
                results = Query.getResults();
                
                   
                    while(results.next()){
                        TimeKeeper timeKeep = new TimeKeeper();
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
                        Time utcStart = results.getTime("start");
                        Time utcEnd = results.getTime("end");  
                        Date dbSqlDate = results.getDate("start");
                        OffsetDateTime offSetStartCompare = OffsetDateTime.parse(start);//Entered Info
                        OffsetDateTime offSetEndCompare = OffsetDateTime.parse(end);//Entered Info
                    
                        OffsetDateTime offSetStart = timeKeep.convertToLocale(utcStart, dbSqlDate);
                        OffsetDateTime offSetEnd = timeKeep.convertToLocale(utcEnd, dbSqlDate);
                        OffsetDateTime utcOffStart = timeKeep.convertToUTC(offSetStart);//DB convertered to local time.
                        OffsetDateTime utcOffEnd = timeKeep.convertToUTC(offSetEnd);//DB Convertered to local time.
                        
                        if(offSetStartCompare.isBefore(utcOffStart) && offSetEndCompare.isBefore(utcOffStart)){
                            //Has no over lap, both start before this appointment.                            
                        }else if(offSetStartCompare.isAfter(utcOffEnd) && offSetEndCompare.isAfter(utcOffEnd)){
                            //Has no over lap, both start after this appointment.
                        }else if(offSetStartCompare.isEqual(utcOffEnd)){
                            //Has no over lap, New appointment start when last one ends.
                        }else if(offSetEndCompare.isEqual(utcOffStart)){
                            //Has no over lap, New appointment ends when last one starts.
                        }else if(offSetStartCompare.isBefore(utcOffStart) && offSetEndCompare.isAfter(utcOffStart)){
                            //Has OVER LAP, last appointment starts in the middle of new appointment.
                            hasOverLap = true;
                            Alert alert = new Alert(CONFIRMATION);
                             alert.setContentText("Appointment can not be set, conflicts with an appointment starting at: " + offSetStart.toString().substring(11, 16) + " and ending at: " +
                                     offSetEnd.toString().substring(11, 16));
                             alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
                             results.close();
                        }else if(offSetStartCompare.isAfter(utcOffStart) && (offSetEndCompare.isBefore(utcOffEnd) || offSetEndCompare.equals(utcOffEnd))){
                            //Has OVER LAP, New appointment is in the middle of the last one.
                            hasOverLap = true;
                            Alert alert = new Alert(CONFIRMATION);
                             alert.setContentText("Appointment can not be set, conflicts with an appointment starting at: " + offSetStart.toString().substring(11, 16) + " and ending at: " +
                                     offSetEnd.toString().substring(11, 16));
                             alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
                             results.close();
                        }else if (offSetStartCompare.isBefore(utcOffStart) && offSetEndCompare.isAfter(utcOffEnd)){
                            //Has OVER LAP, New Appointment cover old appointment.
                            hasOverLap = true;
                            Alert alert = new Alert(CONFIRMATION);
                             alert.setContentText("Appointment can not be set, conflicts with an appointment starting at: " + offSetStart.toString().substring(11, 16) + " and ending at: " +
                                     offSetEnd.toString().substring(11, 16));
                             alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
                             results.close();
                        }else if (offSetStartCompare.isAfter(utcOffStart) && offSetStartCompare.isBefore(utcOffEnd) && offSetEndCompare.isAfter(utcOffEnd)){
                            //Has OVER LAP, New appoint start in the middle of old appointment.
                            hasOverLap = true;
                            Alert alert = new Alert(CONFIRMATION);
                             alert.setContentText("Appointment can not be set, conflicts with an appointment starting at: " + offSetStart.toString().substring(11, 16) + " and ending at: " +
                                     offSetEnd.toString().substring(11, 16));
                             alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
                             results.close();
                        }else if (offSetStartCompare.isEqual(utcOffStart)){
                            //Has OVER LAP, new appointment at same time as old.
                            hasOverLap = true;
                            Alert alert = new Alert(CONFIRMATION);
                             alert.setContentText("Appointment can not be set, conflicts with an appointment starting at: " + offSetStart.toString().substring(11, 16) + " and ending at: " +
                                     offSetEnd.toString().substring(11, 16));
                             alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
                             results.close();
                        }
                }
               
                
            DBConnection.closeConnection();
            
        } catch (SQLException ex) {
            Logger.getLogger(TimeKeeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TimeKeeper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasOverLap;
    }
    
}


