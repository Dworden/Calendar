/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Customer;
import Model.DBConnection;
import Model.Entries;
import Model.Query;
import Model.TimeKeeper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
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
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;



/**
 * FXML Controller class
 *
 * @author Dustin
 */



public class ReportsController implements Initializable {
    
    
    @FXML
    private final Entries entries = new Entries();
  
    
    @FXML
    private Label sceneLabel;

    @FXML
    private Button typeBtn;

    @FXML
    private Button scheduleBtn;

    @FXML
    private Button logBtn;
    
    
    @FXML
    private Label correctLbl;

    @FXML
    private Label failedLbl;

    @FXML
    private Label correctCountLbl;

    @FXML
    private Label failedCountLbl;
    
    @FXML
    private TableView<String> loginTable;
    
    @FXML
    private TableColumn<String, String> correctColumn;

    @FXML
    private TableColumn<String, String> incorrectColumn;
    
    @FXML
    private  ListView<String> correctList; 

    @FXML
    private ListView<String> incorrectList;
    
    @FXML
    private Label typeLbl;

    @FXML
    private ListView<String> typeList;
    
    @FXML
    private ChoiceBox<String> customerChoice;
    
    
    
    @FXML
    private final String fileName = "User_Log.txt";
    
    @FXML
    private final String errorFile = "Failed_Log.txt";
    
       
    @FXML
    void appointmentForm(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;

        stage = (Stage) sceneLabel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentForm.fxml"));
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
    void userForms(ActionEvent event) throws IOException {
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
    void closeButtonAction(ActionEvent event) throws IOException {
        Platform.exit();
    }
    
    @FXML
    void scheduleButtonAction(ActionEvent event) {
        
        int customerId = 0;
        
        //Hide Login list/labels
        correctLbl.setVisible(false);
        failedLbl.setVisible(false);
        correctCountLbl.setVisible(false);
        failedCountLbl.setVisible(false);
        correctList.setVisible(false);
        incorrectList.setVisible(false);
        
        //Hide Type/Month list/labels
        typeLbl.setVisible(false);
        typeList.setVisible(false);
        typeList.getItems().clear();
        
        /**/
        
        if (customerChoice.getValue() == null){
            Alert alert = new Alert(CONFIRMATION);
                    alert.setContentText("Please be sure to select the customer you would like the schedule for.");
                    alert.showAndWait().filter(response -> response == ButtonType.OK);
        } else{
            try {
                String name = customerChoice.getValue();
                
                
                for (int i = 0; i < entries.getCustomerList().size(); i++){
                    Customer myCustomer = (Customer) entries.getCustomerList().get(i);
                    if(myCustomer.getName().equals(name)){
                        customerId = myCustomer.getID();
                    }
                }
                
                ObservableList<String> resultString = FXCollections.observableArrayList();
                
                String q = "SELECT customerId, type,  start, end FROM appointment\n" +
                        "WHERE customerId = '" + customerId +"';";
                
                ResultSet result;
                
                DBConnection.makeConnection();
                Query.makeQuery(q);
                result = Query.getResults();
                
                while(result.next()){
                    TimeKeeper timeKeep = new TimeKeeper();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
                
                    Time utcStart = result.getTime("start");
                    Time utcEnd = result.getTime("end");  
                    Date dbSqlDate = result.getDate("start");
                    
                    OffsetDateTime offSetStart = timeKeep.convertToLocale(utcStart, dbSqlDate);
                    OffsetDateTime offSetEnd = timeKeep.convertToLocale(utcEnd, dbSqlDate);
                                               
                    String string = result.getString("type") + " Starting at: " + format.format(offSetStart) + " Ending at: " + format.format(offSetEnd) + " on: " + result.getDate("start");
                    
                    resultString.add(string);
                }
                
                DBConnection.closeConnection();
                typeList.setItems(resultString);
                typeList.setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        }
        
    }

    @FXML
    void typeButtonAction(ActionEvent event) {
        try {
            
            //Hide Login List/Labels
            correctLbl.setVisible(false);
            failedLbl.setVisible(false);
            correctCountLbl.setVisible(false);
            failedCountLbl.setVisible(false);
            correctList.setVisible(false);
            incorrectList.setVisible(false);
            
            //Hide Schedule List/Labels
            customerChoice.setValue(null);
            
            //Show Type List/Labels
            typeList.getItems().clear();
            typeList.setVisible(true);
            typeLbl.setVisible(true);
            
            ObservableList<String> resultString = FXCollections.observableArrayList();
            
            
            String q = "SELECT type, COUNT(*), MONTHNAME(start) MONTH \n" +
                    "FROM appointment \n" +
                    "GROUP BY type, month(start) \n " +
                    "ORDER BY MONTH(start);";
            
            ResultSet results;
            DBConnection.makeConnection();
            Query.makeQuery(q);
            
            results = Query.getResults();
            
            while (results.next()){ 
                String string = "There are: " + results.getInt("COUNT(*)") +" " + results.getString("type") + " appointments for "+ results.getString("MONTH") +".";
                resultString.add(string);
                
            }
            DBConnection.closeConnection();
            typeList.setItems(resultString);
           
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void logButtonAction(ActionEvent event) throws FileNotFoundException, IOException {
        //Hide Type/Month Appointments.
        typeLbl.setVisible(false);
        typeList.setVisible(false);
        
        //Hide Schedule List/Labels
        customerChoice.setValue(null);
        
        ObservableList<String> incorrect = FXCollections.observableArrayList();
        ObservableList<String> correct = FXCollections.observableArrayList();
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        BufferedReader in2 = new BufferedReader(new FileReader( errorFile));
        int correctCount = 0;
        int incorrectCount = 0;
        
        
        
        while (in.ready()){
            String correctLog = in.readLine();
            correct.add(correctLog);
            correctCount++;
        }
        
        while(in2.ready()){
            String incorrectLog = in2.readLine();
            incorrect.add(incorrectLog);
            incorrectCount++;
        }
        
        
        correctList.setItems(correct);
        incorrectList.setItems(incorrect);
        correctCountLbl.setText("Count: " + correctCount);
        failedCountLbl.setText("Count: " + incorrectCount);
        
        correctLbl.setVisible(true);

        failedLbl.setVisible(true);

        correctCountLbl.setVisible(true);

        failedCountLbl.setVisible(true);
    
        correctList.setVisible(true);
    
        incorrectList.setVisible(true);
       

    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    ObservableList<String> names = FXCollections.observableArrayList();
    
    //Hide Login list/labels.
    correctLbl.setVisible(false);

    failedLbl.setVisible(false);

    correctCountLbl.setVisible(false);

    failedCountLbl.setVisible(false);
    
    correctList.setVisible(false);
    
    incorrectList.setVisible(false);
    
    //Hide Type/Month list/labels
    
    typeLbl.setVisible(false);
    
    typeList.setVisible(false);
    
    for(int i = 0; i < entries.getCustomerList().size(); i++){
        Customer myCustomer = (Customer) entries.getCustomerList().get(i);
        names.add(myCustomer.getName());
        
    }
    customerChoice.setItems(names);
    }   
    
}
