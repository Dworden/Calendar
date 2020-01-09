/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointments;
import Model.Customer;
import Model.Entries;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dustin
 */
public class CalendarController implements Initializable {

    @FXML
    private Label sceneLabel;

    @FXML
    private Tab week_tab;

    @FXML
    private TableView<Appointments> week_table;

    @FXML
    private TableColumn<Appointments, Date> dateColumn;

    @FXML
    private TableColumn<Appointments, String> startColumn;

    @FXML
    private TableColumn<Appointments, String> endColumn;

    @FXML
    private TableColumn<Appointments, String> customerColumn;

    @FXML
    private TableColumn<Appointments, String> typeColumn;

    @FXML
    private Tab month_tab;

    @FXML
    private TableView<Appointments> month_table;

    @FXML
    private TableColumn<Appointments, Date> monthDateColumn;

    @FXML
    private TableColumn<Appointments, String> monthStartColumn;

    @FXML
    private TableColumn<Appointments, String> monthEndColumn;

    @FXML
    private TableColumn<Appointments, String> monthCustomerColumn;

    @FXML
    private TableColumn<Appointments, String> monthTypeColumn;
    
    @FXML
    private final Entries entries = new Entries();
    
    @FXML
    private final  ObservableList customerList = entries.getCustomerList();

    @FXML
    void closeButtonAction(ActionEvent event) throws IOException {
        Platform.exit();
    }

    @FXML
    void createButtonAction(ActionEvent event) {

    }

    @FXML
    void deleteButtonAction(ActionEvent event) {

    }

    @FXML
    void editButtonAction(ActionEvent event) {

    }

    @FXML
    void loadWeeklyAppointments(Event event) {
                    week_table.refresh();
                    week_table.getItems().clear();
                    for (int i = 0; i < customerList.size(); i++) {
                        Customer myCustomer = (Customer) customerList.get(i);
                        
                        for (int j = 0; j < myCustomer.getAppointmentList().size(); j++) {
                            Appointments appointments = (Appointments) myCustomer.getAppointment(j);
                                                       System.out.println("My customer: " + myCustomer.getName() + " Appointment sizes: "+ myCustomer.getAppointmentList().size()
                                                       + " Appointment id: "  +appointments.getId() + " Appointment(UTC START): "+ appointments.getStartTime() + " Appointment(Offset Start)" + appointments.getFormatStart()
                                                       + "Appointment(UTC END) " + appointments.getEndTime() + "Appointment(offset End) "+ appointments.getFormatEnd());
                            OffsetDateTime offsetDate = appointments.getStartTime();
                            OffsetDateTime current = OffsetDateTime.now();
                            OffsetDateTime oneWeek = current.plusWeeks(1);
                            
                            if (offsetDate.isAfter(current.minusDays(1)) && offsetDate.isBefore(oneWeek)){
                              appointments.setFormatStart(appointments.getStartTime());
                              appointments.setFormatEnd(appointments.getEndTime());
                              week_table.getColumns().setAll(dateColumn, startColumn, endColumn, customerColumn, typeColumn);
                              dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                              startColumn.setCellValueFactory(new PropertyValueFactory<>("formatStart"));
                              endColumn.setCellValueFactory(new PropertyValueFactory<>("formatEnd"));
                              customerColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                              typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                              week_table.getItems().add(appointments);
                            }
                        }
                        
                    }
                    
                    
    }
        

    @FXML
    void loadMonthlyAppointments(Event event) {
                    
                    month_table.getItems().clear();
                    for (int i = 0; i < customerList.size(); i++) {
                        Customer myCustomer = (Customer) customerList.get(i);
                        for (int j = 0; j < myCustomer.getAppointmentList().size(); j++) {
                            Appointments appointments = (Appointments) myCustomer.getAppointment(j);
                                                       
                            OffsetDateTime offsetDate = appointments.getStartTime();
                            OffsetDateTime current = OffsetDateTime.now();
                            
                              offsetDate.getMonth().equals(current.getMonth());                         
                            if (offsetDate.getMonth().equals(current.getMonth())){
                              appointments.setFormatStart(appointments.getStartTime());
                              appointments.setFormatEnd(appointments.getEndTime());
                              month_table.getColumns().setAll(dateColumn, startColumn, endColumn, customerColumn, typeColumn);
                              dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                              startColumn.setCellValueFactory(new PropertyValueFactory<>("formatStart"));
                              endColumn.setCellValueFactory(new PropertyValueFactory<>("formatEnd"));
                              customerColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                              typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
                              month_table.getItems().add(appointments);
                            }
                     }
                           
                    }
                    
                    
    }

    //Navigation Menu
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
    void reports(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;
        
        stage = (Stage) sceneLabel.getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Reports.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        month_table.refresh();
        week_table.refresh();
        
        
      

    }

}
