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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dustin
 */
public class MainController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private Button login_btn;

    @FXML
    private PasswordField password_txt;

    @FXML
    private TextField user_txt;
    
    @FXML
    private String userDB;
    
    @FXML
    private String passwordDB;
    
    @FXML
    private String q = "SELECT userName, password FROM U05EJ7.user;";
    
    @FXML
    private final ResourceBundle rb = ResourceBundle.getBundle("lib/Text", Locale.getDefault());
    
    @FXML
    private final Entries entries = new Entries();
    
    @FXML
    private ObservableList customerList = FXCollections.observableArrayList();
    
    
    @FXML
    private void logIn(ActionEvent event) throws IOException, SQLException {
        
        //Get input. Sanatize input as both user name and password are stored lower cased.
        String user = user_txt.getText().toLowerCase();
        String password = password_txt.getText().toLowerCase();
        //TimeKeeper Log for logging proper and failed log-ins.
        TimeKeeper log = new TimeKeeper();
        
        //CONNECT TO DATABASE AND PULL INFO
        //<editor-fold>
        DBConnection.makeConnection();
        
        Query.makeQuery(q);
        ResultSet result = Query.getResults();
        
        while (result.next()){
            userDB = result.getString("userName");
            passwordDB = result.getString("password");
        }
        //</editor-fold>
        
        
        
        //Compare entered text to stored database. Handle alerts for Incorrect credentials. Load Calendar.FXML.
        //<editor-fold>
        if ((user.equals(userDB)) && password.equals(passwordDB)){
            
            //Successful Log-in, Log Current time, [Close Database Connection, Load Calendar.fxml] - [Try Catch]
            //<editor-fold>
            
            log.Log();
            
           
            try {
                createCustomers();
                createAppointments();
                DBConnection.closeConnection();
                Stage stage;
                Parent root;
                
                stage = (Stage) login_btn.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
                root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //</editor-fold>
        } else if (user.equals(userDB) && !password.equals(passwordDB)){
            
            //[Correct User Name/Incorrect Password, Translate Alert Msg to Locale Language if needed.] - [Try Catch]
            //<editor-fold>
            //Log error
            log.LogError();
            try {
                 if (Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("fr"))
                  {

                    Alert alert = new Alert(CONFIRMATION);
                    alert.setContentText(rb.getString("User") + " " + rb.getString("Name") + " " + rb.getString("Password") + " " + rb.getString("Incorrect") );
                    alert.showAndWait().filter(response -> response == ButtonType.OK);

                  } else { 
                     //Create Lambda alert for incorrect Password.
                     Alert alert = new Alert(CONFIRMATION);
                     alert.setContentText("User name and Password incorrect.");
                     alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> System.out.println("Password incorrect."));
                     }
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //</editor-fold>
        
        } else {
            //[Incorrect User Name/Correct Password, Translate Alert Msg to Locale Language if needed.] - [Try Catch]
            //<editor-fold>
            //Log error
            log.LogError();
            try {
                
                
                  if (Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("fr"))
                  {

                    Alert alert = new Alert(CONFIRMATION);
                    alert.setContentText(rb.getString("User") + " " + rb.getString("Name") + " " + rb.getString("Password")+ " " +rb.getString("Incorrect") );
                    alert.showAndWait().filter(response -> response == ButtonType.OK);

                  } else { 
                
                      //Create user name alert.
                      Alert alert = new Alert(CONFIRMATION);
                      alert.setContentText("User name and Password incorrect.");
                      alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> System.out.println("User name wrong."));
                  }
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //</editor-fold>
        }
        //</editor-fold>
        
    }
    
    @FXML
    private void createCustomers(){
        
        try {
            String q =  "SELECT customer.customerId, customer.customerName, address.addressId, address.address, address.address2, address.phone, address.postalCode, city.cityId, city.city, country.countryId, country.country \n" +
                        "FROM customer \n" +
                        "LEFT JOIN address on customer.addressId = address.addressId \n" +
                        "LEFT JOIN city on address.cityId = city.cityId \n" +
                        "LEFT JOIN country on city.countryId = country.countryId;";
            
            ResultSet results;
            Query.makeQuery(q);
            results = Query.getResults();
            
            while(results.next()){
                
                Customer myCustomer = new Customer(results.getInt("customerId"), results.getString("customerName"), results.getString("address"), results.getString("address2"),
                results.getString("city"), results.getString("postalCode"), results.getString("phone"), results.getString("country"), results.getInt("addressId"), results.getInt("cityId"),
                results.getInt("countryId"));
                
                entries.addCustomerEntry(myCustomer);
                              
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    @FXML
    private void createAppointments() throws ParseException{
        
        try {  
            ResultSet results;
            customerList = entries.getCustomerList();
            
            String q = "SELECT customer.customerName, start, end, type, appointmentId \n"
                    + "FROM appointment\n" + "INNER JOIN customer\n"
                    + "ON appointment.customerId = customer.customerId;\n";
          
            
            
            Query.makeQuery(q);
            results = Query.getResults();
            while (results.next()){                
                
                //UTC Time Pull/Convert to local time.
                //<editor-fold>               
                //Varibles
                TimeKeeper timeKeep = new TimeKeeper();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
                
                Time utcStart = results.getTime("start");
                Time utcEnd = results.getTime("end");  
                Date dbSqlDate = results.getDate("start");
                                               
                OffsetDateTime offSetStart = timeKeep.convertToLocale(utcStart, dbSqlDate);
                OffsetDateTime offSetEnd = timeKeep.convertToLocale(utcEnd, dbSqlDate);
                OffsetDateTime utcOffStart = timeKeep.convertToUTC(offSetStart);
                OffsetDateTime utcOffEnd = timeKeep.convertToUTC(offSetEnd);
                
                                
                Appointments myAppointment = new Appointments(offSetStart, offSetEnd, results.getString("type"), results.getDate("start"),
                results.getString("customerName"), Integer.parseInt(results.getString("appointmentId")));
                 
                myAppointment.setFormatStart(offSetStart);
                myAppointment.setFormatEnd(offSetEnd);
                
                for (int i = 0; i <customerList.size(); i++){
                    
                    Customer customer = (Customer) customerList.get(i);
                    
                    if(customer.getName().equals(results.getString("customerName"))){
                        
                        customer.addAppointment(myAppointment);
                        if(timeKeep.withinTime(offSetStart)){
                             Alert alert = new Alert(CONFIRMATION);
                             alert.setContentText("Appointment within 15 minutes. With: " + customer.getName() + " at: " + format.format(offSetStart));
                             alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> System.out.println("Appointment in 15 minutes."));
                        }
                      }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO   
        rb = ResourceBundle.getBundle("lib/Text", Locale.getDefault());

        
        if (Locale.getDefault().getLanguage().equals("de") || Locale.getDefault().getLanguage().equals("fr")){
            user_txt.setPromptText(rb.getString("User")+ " " +rb.getString("Name") );
            password_txt.setPromptText(rb.getString("Password"));
            login_btn.setText(rb.getString("Log") + rb.getString("In"));
        }
    }      
    
    
}
