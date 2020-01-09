/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointments;
import Model.Customer;
import Model.DBConnection;
import Model.Entries;
import Model.Query;
import Model.TimeKeeper;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dustin
 */
public class AppointmentFormController implements Initializable {
    
    @FXML
    private Label sceneLabel;
    
     @FXML
    private TextField customerName_txt;
     
    @FXML
    private DatePicker datePicker;
    
    
    @FXML
    private Button newCust_button;

    @FXML
    private Button newAppt_button;
    
    @FXML
    private Button delete_button;

    @FXML
    private Button update_btn;
    
    @FXML
    private TableView <Customer> customer_tbl;
    
    @FXML
    private TableColumn<Customer, String> aptCustomer;
    
    @FXML 
    private TableView <Appointments> appointment_tbl;
    
    @FXML
    private TableColumn<Appointments, Date> dateColumn;

    @FXML
    private TableColumn<Appointments, String> startColumn;

    @FXML
    private TableColumn<Appointments, String> endColumn;

    @FXML
    private TableColumn<Appointments, String> typeColumn;       

    @FXML
    private ChoiceBox<String> startChoice;

    @FXML
    private ChoiceBox<String> endChoice;

    @FXML
    private ChoiceBox<String> aptChoice;
    
    @FXML
    private Label appointment_lbl;
    
    @FXML
    private final Entries entries = new Entries();
    
    @FXML
    private final ObservableList customerList = entries.getCustomerList();
    
    @FXML
    private final ObservableList appointmentList = entries.getAppointmentList();
    
    @FXML
    private Customer customer = new Customer();
    
    @FXML
    private Appointments appointment = new Appointments();
    
    
    
    
            

   
    
    
    @FXML
    void closeButtonAction(ActionEvent event) throws IOException{
        Platform.exit();
    }
    
    @FXML
    void createButtonAction(ActionEvent event){
        
        
    }
    
    @FXML
    void deleteButtonAction(ActionEvent event){
        try {
            customer = customer_tbl.getSelectionModel().getSelectedItem();
            appointment = appointment_tbl.getSelectionModel().getSelectedItem();
            
            Appointments compare;// = new Appointments();
            
            String q = "DELETE FROM U05EJ7.appointment \n"
                    + "WHERE appointmentId = " + appointment.getId();
            
            
            DBConnection.makeConnection();
            Query.makeQuery(q);
            DBConnection.closeConnection();
            //Got to Correctly remove appointment. Get id isnt working.
            for(int i = 0; i < customer.getAppointmentList().size(); i++){
                compare = customer.getAppointment(i);
                if(appointment.getId() == compare.getId()){
                    customer.removeAppointment(i);
                }
                
            }
            
            //Remove from master entries list.
            for (int i = 0; i < entries.getAppointmentList().size(); i++){
                compare = (Appointments) entries.getAppointmentList().get(i);
                if(appointment.getId() == compare.getId()){
                    entries.removeAppointmentEntry(appointment.getId());
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AppointmentFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        appointment_tbl.getItems().clear();
            ObservableList appointments = customer.getAppointmentList();
            
            for (int i = 0; i < appointments.size(); i++){
                 appointment = customer.getAppointment(i);
                 appointment_tbl.getColumns().setAll(dateColumn, startColumn, endColumn, typeColumn);
                 dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                 startColumn.setCellValueFactory(new PropertyValueFactory<>("formatStart"));
                 endColumn.setCellValueFactory(new PropertyValueFactory<>("formatEnd"));
                 typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                 appointment_tbl.getItems().add(appointment);
            }
    }
    
    @FXML
    void editButtonAction(ActionEvent event){
        
    }
    
    @FXML
    void updateButtonAction(ActionEvent event) {
     
        TimeKeeper overLap = new TimeKeeper();
        customer = customer_tbl.getSelectionModel().getSelectedItem();
        appointment = appointment_tbl.getSelectionModel().getSelectedItem();
        Color color = Color.DARKGREEN;
        appointment_lbl.setTextFill(color);
        appointment_lbl.setText("Appointment updated successfully.");
     
        LocalDate local = datePicker.getValue();
        Date date = Date.valueOf(local);
        
         String sqlStartTime = date.toString() + "T" + startChoice.getValue();
         String sqlEndTime = date.toString() + "T" + endChoice.getValue();
         
         String offset  = ZonedDateTime.now().getOffset().getId();
                           
         OffsetDateTime offSetStart = OffsetDateTime.parse(sqlStartTime + offset);
         OffsetDateTime offSetEnd = OffsetDateTime.parse(sqlEndTime + offset);
         
         OffsetDateTime offStartUTC = overLap.convertToUTC(offSetStart);
         OffsetDateTime offEndUTC = overLap.convertToUTC(offSetEnd);
         
         String stringOffSetStart = offStartUTC.toString();
         String stringOffSetEnd = offEndUTC.toString();
         
         
         
         System.out.println( sqlStartTime + " :SQL START AND END: " + sqlEndTime + " ///// " + offSetStart + " :OFFSETSTART and END: " + offSetEnd +
                 " //////// " + offStartUTC + " :OFFSET UTC AND END UTC: " + offEndUTC + " //// " + stringOffSetStart + " :STRING START AND END: " +
                 stringOffSetEnd);
         
        
      
        //TODO Switch appointment time with choice box value.
        if(overLap.overLapAppointments(stringOffSetStart ,stringOffSetEnd , date)){
            
            color = Color.RED;
            appointment_lbl.setTextFill(color);
            appointment_lbl.setText("Appoinment conflicts with another appointment.");
        }else{
                        
            String q = "UPDATE `U05EJ7`.`appointment` SET `type` = '"+aptChoice.getValue()+"', `start` = '"+stringOffSetStart.substring(0,16)+"', `end` = '"+stringOffSetEnd.substring(0,16)+"' WHERE (`appointmentId` = '"+appointment.getId()+"');";
        
            for(int i = 0; i < appointmentList.size(); i++){
                Appointments compare = (Appointments) entries.getAppointmentList().get(i);
                if(compare.getId() == appointment.getId()){
                    
                    entries.removeAppointmentEntry(i);
                    entries.addAppointmentEntry(appointment);
                    
                }
            }
            
            for(int i = 0; i < customer.getAppointmentList().size(); i++){
                Appointments compare = customer.getAppointment(i);
                if(compare.getId() == appointment.getId()){
                    customer.removeAppointment(i);
                    customer.addAppointment(appointment);
                    
                    
                }
            }
            appointment.setDate(date);
            appointment.setStartTime(offSetStart);
            appointment.setEndTime(offSetEnd);
            appointment.setFormatStart(offSetStart);
            appointment.setFormatEnd(offSetEnd);
            appointment.setAppointmentType(aptChoice.getValue());
            
            
            DBConnection.makeConnection();
            Query.makeQuery(q);
            try {
                DBConnection.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(AppointmentFormController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(AppointmentFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            appointment_tbl.getItems().clear();
            ObservableList appointments = customer.getAppointmentList();
           
            for (int i = 0; i < appointments.size(); i++){
                 appointment = customer.getAppointment(i);
                 
                 appointment_tbl.getColumns().setAll(dateColumn, startColumn, endColumn, typeColumn);
                 dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                 startColumn.setCellValueFactory(new PropertyValueFactory<>("formatStart"));
                 endColumn.setCellValueFactory(new PropertyValueFactory<>("formatEnd"));
                 typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                 appointment_tbl.getItems().add(appointment);
            }
            
            color = Color.ROYALBLUE;
            appointment_lbl.setTextFill(color);
            appointment_lbl.setText("Appointment updated! " + appointment.getFormatStart() + " :Start---End: " + appointment.getFormatEnd());
        }
        

    }
    //New Customer
    @FXML
    void customerButtonAction(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;
            
        stage = (Stage) sceneLabel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserForms.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //New Appointment.
    @FXML
    void appointmentButtonAction(ActionEvent event) {
        
        
        if(startChoice.getValue() == null|| endChoice.getValue() == null|| aptChoice.getValue()== null || datePicker.getValue() == null){
           
                     
            Color color = Color.RED;
            appointment_lbl.setTextFill(color);
            appointment_lbl.setText("Please verify all values entered.");
            
        }else{
            try {
                
                TimeKeeper overLap = new TimeKeeper();
                customer = customer_tbl.getSelectionModel().getSelectedItem();
                LocalDate local = datePicker.getValue();
                Date date = Date.valueOf(local);
                String sqlStartTime = date.toString() + "T" + startChoice.getValue();
                String sqlEndTime = date.toString() + "T" + endChoice.getValue();
         
                String offset  = ZonedDateTime.now().getOffset().getId();
         
               
        
                OffsetDateTime offSetStart = OffsetDateTime.parse(sqlStartTime + offset);
                OffsetDateTime offSetEnd = OffsetDateTime.parse(sqlEndTime + offset);
         
                OffsetDateTime offStartUTC = overLap.convertToUTC(offSetStart);
                OffsetDateTime offEndUTC = overLap.convertToUTC(offSetEnd);
         
                String stringOffSetStart = offStartUTC.toString();
                String stringOffSetEnd = offEndUTC.toString();
               
                TimeKeeper convertor = new TimeKeeper();
                java.util.Date createdDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
                Timestamp createDate = new java.sql.Timestamp(createdDate.getTime());
                ObservableList appointments = customer.getAppointmentList();
                
                if(overLap.overLapAppointments(stringOffSetStart ,stringOffSetEnd , date)){
            
               Color color = Color.RED;
               appointment_lbl.setTextFill(color);
               appointment_lbl.setText("Appoinment conflicts with another appointment.");
               }else{
                
                String q = "INSERT INTO U05EJ7.appointment (customerId, userId,title, description, location, contact,\n"
                        +"type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)"
                        +"VALUES(" + customer.getID() + ",'1','not needed', 'not needed', 'not needed', 'not needed','"
                        +aptChoice.getValue() + "','not needed','" + stringOffSetStart.substring(0,16) + "','" + stringOffSetEnd.substring(0,16)
                        +"','" + createDate +"','test','" + createDate +"','test');";
                
                DBConnection.makeConnection();
                Query.makeQuery(q);
                DBConnection.closeConnection();
                Appointments myAppointment = new Appointments(offSetStart, offSetEnd, aptChoice.getValue(), date, customer.getName(), appointmentList.size());
                myAppointment.setFormatStart(offSetStart);
                myAppointment.setFormatEnd(offSetEnd);
                
                
                customer.addAppointment(myAppointment);
                entries.addAppointmentEntry(myAppointment);
                appointment_tbl.getItems().clear();
        
                for (int i = 0; i < appointments.size(); i++){
                myAppointment = customer.getAppointment(i);
                System.out.println("Appointment: " + myAppointment.getDate() + " " + myAppointment.getFormatStart() + " " + myAppointment.getFormatEnd()
                + " " + myAppointment.getName() + " " + myAppointment.getAppointmentType());
                appointment_tbl.getColumns().setAll(dateColumn, startColumn, endColumn, typeColumn);
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                startColumn.setCellValueFactory(new PropertyValueFactory<>("formatStart"));
                endColumn.setCellValueFactory(new PropertyValueFactory<>("formatEnd"));
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                appointment_tbl.getItems().add(myAppointment);
            
        }
                
                Color color = Color.DARKGREEN;
                appointment_lbl.setTextFill(color);
                appointment_lbl.setText("Appointment created.");
               } 
             
            } catch (SQLException ex) {
                Logger.getLogger(AppointmentFormController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(AppointmentFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

    }
    
    
    //Screen Control Methods, userForms, 
    
    @FXML
    void userForms(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;
            
        stage = (Stage) sceneLabel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserForms.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void calendar(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;
            
        stage = (Stage) sceneLabel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void mouseClicked(MouseEvent event){
        //appointment_lbl.setVisible(false);
        Color color = Color.DARKBLUE;
        appointment_lbl.setTextFill(color);
        appointment_lbl.setText("Please select an appointment or create a new appointment.");
        
        customer = customer_tbl.getSelectionModel().getSelectedItem();
        datePicker.setValue(null);
        startChoice.setValue(null);
        endChoice.setValue(null);
        aptChoice.setValue(null);
        
        //customerName_txt.setText(customer.getName());
        String name = customer.getName();
        ObservableList appointments = customer.getAppointmentList();
        customerName_txt.setText(name);
        appointment_tbl.getItems().clear();
        System.out.println("Size: " + appointments.size());
        for (int i = 0; i < appointments.size(); i++){
            Appointments appointment = customer.getAppointment(i);
            System.out.println("Appointment: " + appointment.getDate() + " " + appointment.getFormatStart() + " " + appointment.getFormatEnd()
            + " " + appointment.getName() + " " + appointment.getAppointmentType());
            appointment_tbl.getColumns().setAll(dateColumn, startColumn, endColumn, typeColumn);
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            startColumn.setCellValueFactory(new PropertyValueFactory<>("formatStart"));
            endColumn.setCellValueFactory(new PropertyValueFactory<>("formatEnd"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
            appointment_tbl.getItems().add(appointment);
            
        }
        
        
    }
    
    @FXML
    void onMouseClicked(MouseEvent event){
        
        Appointments appointment = appointment_tbl.getSelectionModel().getSelectedItem();
        startChoice.setValue(appointment.getFormatStart());
        System.out.println("Formats: " + appointment.getFormatEnd() + ":End, Start: " + appointment.getFormatStart());
        endChoice.setValue(appointment.getFormatEnd());
        Date date = (Date) appointment.getDate();
        LocalDate localDate = date.toLocalDate();
        datePicker.setValue(localDate);
        Color color = Color.RED;
        appointment_lbl.setTextFill(color);
        appointment_lbl.setText("Appointment ready to update.");
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        startChoice.setItems(FXCollections.observableArrayList("08:00","08:15","08:30","08:45","09:00", "09:15", "09:30", "09:45", "10:00",
                "10:15" ,"10:30", "10:45" ,"11:00", "11:15", "11:30" ,"11:45 ","12:00" ,"12:15","12:30" ,"12:45","13:00"));
        
        endChoice.setItems(FXCollections.observableArrayList("08:15","08:30","08:45","09:00", "09:15", "09:30", "09:45", "10:00",
                "10:15" ,"10:30", "10:45","11:00" ,"11:15" ,"11:30" ,"11:45" ,"12:00","12:15","12:30","12:45","13:00" ,"13:15", "13:30", "13:45","14:00"));
        
        aptChoice.setItems(FXCollections.observableArrayList("Personal","Weekly","Monthly","As Needed", "Followup", "New Appt"));
        
        customer_tbl.getItems().clear();
        for (int i = 0; i < customerList.size(); i++) {
                        Customer myCustomer = (Customer) customerList.get(i);
                        customer_tbl.getColumns().setAll(aptCustomer);
                        aptCustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
                        customer_tbl.getItems().add(myCustomer);
        }
        Color color = Color.DARKBLUE;
        appointment_lbl.setTextFill(color);
        appointment_lbl.setText("Please select a Customer.");
    }    
    
}
