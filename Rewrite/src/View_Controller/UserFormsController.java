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
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import static java.sql.Types.NULL;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dustin
 */
public class UserFormsController implements Initializable {
    
    @FXML
    private Label sceneLabel;
    
    @FXML
    private TableView <Customer> customer_tbl;
    
    @FXML
    private TextField name_txt;
    
    @FXML
    private TextField ad1_txt;
    
    @FXML
    private TextField ad2_txt;
    
    @FXML
    private TextField city_txt;
    
    @FXML
    private TextField zip_txt;
    
    @FXML
    private TextField phone_txt;
    
    @FXML
    private ComboBox country_box;
    
    @FXML
    private Button create_button;
    
    @FXML
    private Button update_button;
    
    @FXML
    private Button delete_button;
    
    @FXML
    private TableColumn <Customer, String> customer_column;
    
    @FXML
    private final Entries entries = new Entries();
    
    @FXML
    private final ObservableList customerList = entries.getCustomerList();
    
    @FXML
    private Customer customer = new Customer();
    
    @FXML
    private int cityId ;
    
    @FXML
    private int addId;
    
    @FXML 
    private int countryId;
    
    //Varibles
    
   @FXML
    void closeButtonAction(ActionEvent event) throws IOException{
        Platform.exit();
    }
    
    @FXML
    void createButtonAction(ActionEvent event) throws SQLException, Exception{
        
        
        //Check entered name against all other customers.
        //<editor-fold>
        int count = 0;
        for (int i = 0; i < customerList.size(); i++){
            Customer name = (Customer) customerList.get(i);
            if (name.getName().equalsIgnoreCase(name_txt.getText()) ){
                
            } else {
                count += 1;
            }
            
        }
        //</editor-fold>
        //<editor-fold>
        
        
        
        
        if(name_txt.getText()== null){
            //Lambda alert quickly tells user to fill in all needed forms.
            Alert alert = new Alert(CONFIRMATION);
            alert.setContentText("Name wasn't filled");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
          
        }else if(ad1_txt.getText()== null){
            //Lambda alert quickly tells user to fill in all needed forms.
            Alert alert = new Alert(CONFIRMATION);
            alert.setContentText("Address wasn't filled");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            
        }else if(city_txt.getText() == null){
            //Lambda alert quickly tells user to fill in all needed forms.
            Alert alert = new Alert(CONFIRMATION);
            alert.setContentText("City wasn't filled");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            
        }else if(zip_txt.getText() == null){
            //Lambda alert quickly tells user to fill in all needed forms.
            Alert alert = new Alert(CONFIRMATION);
            alert.setContentText("Zip wasn't filled");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            
        }else if( phone_txt.getText() == null){
            //Lambda alert quickly tells user to fill in all needed forms.
            Alert alert = new Alert(CONFIRMATION);
            alert.setContentText("Phone wasn't filled");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            
        }else if(country_box.getValue() == null){
            Alert alert = new Alert(CONFIRMATION);
            alert.setContentText("Country wasn't filled");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
        }else{
          //Create new customer, add to table.
          //<editor-fold>
          if(count == customerList.size()){
            
            //Storing in DATABASE
            //<editor-fold>
            ResultSet results;
            
            
            String citiesQ = "SELECT * FROM U05EJ7.city \n" +
                             "WHERE city = '" + city_txt.getText() +"';";
            
            String addressQ = "SELECT * FROM U05EJ7.address \n" +
                              "WHERE address = '" + ad1_txt.getText() +"';";
            
            String countyQ = "SELECT * FROM U05EJ7.country \n" +
                             "WHERE country = '" + country_box.getValue().toString() + "';";
          
            DBConnection.makeConnection();
            
            //Get City Id
            //<editor-fold>
            Query.makeQuery(citiesQ);
            results = Query.getResults();
            if(!results.next()){
                
                String allCities = "SELECT * FROM U05EJ7.city";
                Query.makeQuery(allCities);
                results = Query.getResults();
                int counter = 1;
                while (results.next()){
                    counter += 1;
                }
                cityId = counter;
                
                
                
            } else { results.beforeFirst();
            
                 while(results.next()){
                     cityId = results.getInt("cityId");
                    }
            }
            //</editor-fold>
            
            //Get Address Id
            //<editor-fold>
            Query.makeQuery(addressQ);
            results = Query.getResults();
            if(!results.next()){
                String allAddress = "SELECT * FROM U05EJ7.address";
                Query.makeQuery(allAddress);
                results = Query.getResults();
                int counter = 1;
                while (results.next()){
                    counter += 1;
                    
                }
                addId = counter;
                
                
            } else { results.beforeFirst();
            
                 while(results.next()){
                     addId = results.getInt("addressId");
                     
                    }
            }
            //</editor-fold>
            
            //Get Country ID
            //<editor-fold>
            Query.makeQuery(countyQ);
            results = Query.getResults();
            if(!results.next()){
                
                String allCountries = "SELECT * FROM U05EJ7.country";
                Query.makeQuery(allCountries);
                results = Query.getResults();
                int counter = 1;
                while (results.next()){
                    counter += 1;
                   
                }
                
                countryId = counter;
               
                
                
            } else { results.beforeFirst();
            
                 while(results.next()){
                     countryId = results.getInt("countryId");
                     
                    }
            }
            
            //</editor-fold>
            
            int id = customerList.size() + 1;
            Customer myCustomer = new Customer(id, name_txt.getText(), ad1_txt.getText(), ad2_txt.getText(),
            city_txt.getText(), zip_txt.getText(), phone_txt.getText(), country_box.getValue().toString(), addId, cityId, countryId);
        
            customerList.add(myCustomer);
            
            //Inserting Customer into DB.  
            java.util.Date createdDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
            Timestamp createDate = new java.sql.Timestamp(createdDate.getTime());
             
            
            String customerInfo = "INSERT INTO `U05EJ7`.`customer` (`customerId`, `customerName`, `addressId` ,`active`, `createDate` , `createdBy`, `lastUpdate`, `lastUpdateBy`) "
                                   + "VALUES ('" + myCustomer.getID() +"','" + myCustomer.getName() +"','"+ addId + "'," +"'1 ',' " + createDate + "','test','" + createDate + "','test');";
            
            String addressInfo = "INSERT INTO `U05EJ7`.`address` (`addressId`, `address`, `address2`, `cityId`, `postalCode`, `phone`, `createDate`, `createdBy`, `lastUpdate`, `lastUpdateBy`)"
                                +"VALUES ('"+ addId + "','" + ad1_txt.getText() + "','" + ad2_txt.getText() +"','" + cityId + "','" +zip_txt.getText() +"','" + phone_txt.getText() + "','" + createDate + "','test'," +"'" + createDate +"','test');";
            
            String cityInfo = "INSERT INTO `U05EJ7`.`city` (`cityId`, `city` , `countryId`, `createDate`, `createdBy`, `lastUpdate`, `lastUpdateBy`) VALUES ('" + cityId + "','" + city_txt.getText()+ "','" + countryId + "','" + createDate +"','test'," +"'" + createDate +"','test');";
            Query.makeQuery(cityInfo);
            Query.makeQuery(addressInfo);
            Query.makeQuery(customerInfo);
            DBConnection.closeConnection();
            Alert alert = new Alert(CONFIRMATION);
            alert.setContentText("Customer saved.");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
            //</editor-fold>
            
            
            //Add Customer to table
            customer_tbl.getItems().clear();
            for (int i = 0; i < customerList.size(); i++) {
                            myCustomer = (Customer) customerList.get(i);
                            customer_tbl.getColumns().setAll(customer_column);
                            customer_column.setCellValueFactory(new PropertyValueFactory<>("name"));
                            customer_tbl.getItems().add(myCustomer);
            }
          }else{
              //Lambda Alert, quickly tells user, Customer is already in use.
              Alert alert = new Alert(CONFIRMATION);
              alert.setContentText("Customer: " + name_txt.getText() + " already created.");
              alert.showAndWait().filter(response -> response == ButtonType.OK);
          }
          //</editor-fold>
        }
        //</editor-fold>
        name_txt.setText(null);
        ad1_txt.setText(null);
        ad2_txt.setText(null);
        city_txt.setText(null);
        zip_txt.setText(null);
        phone_txt.setText(null);
        country_box.setValue(null);
    }
    
    @FXML
    void deleteButtonAction(ActionEvent event){
        Entries entry = new Entries();
        
        customer = customer_tbl.getSelectionModel().getSelectedItem();
        
        if(customer.getAppointmentList().isEmpty()){
            
        
        
         Alert alert = new Alert(CONFIRMATION);
         alert.setContentText("Are you sure you want to delete: " + customer.getName());
         Optional<ButtonType> result = alert.showAndWait();
         if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                DBConnection.makeConnection();
                String deleteQ = "DELETE FROM customer WHERE customerId = " + customer.getID() + ";";
                Query.makeQuery(deleteQ);
                entry.removeCustomerEntry(customer.getID()-1);
                DBConnection.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(UserFormsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(UserFormsController.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        } else {
            
            Alert alert = new Alert(CONFIRMATION);
         alert.setContentText("Are you sure you want to delete: " + customer.getName());
         Optional<ButtonType> result = alert.showAndWait();
         if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                DBConnection.makeConnection();
                String deleteApt = "DELETE FROM appointment WHERE customerId = " + customer.getID() + ";";
                String deleteQ = "DELETE FROM customer WHERE customerId = " + customer.getID() + ";";
                Query.makeQuery(deleteApt);
                Query.makeQuery(deleteQ);
                entry.removeCustomerEntry(customer.getID()-1);
                DBConnection.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(UserFormsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(UserFormsController.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
            
        }
        customer_tbl.getItems().clear();
        for (int i = 0; i < customerList.size(); i++) {
                        Customer myCustomer = (Customer) customerList.get(i);
                        customer_tbl.getColumns().setAll(customer_column);
                        customer_column.setCellValueFactory(new PropertyValueFactory<>("name"));
                        customer_tbl.getItems().add(myCustomer);
        }
      
        
        name_txt.setText(null);
        ad1_txt.setText(null);
        ad2_txt.setText(null);
        city_txt.setText(null);
        zip_txt.setText(null);
        phone_txt.setText(null);
        country_box.setValue(null);
                           
    }
    
    @FXML
    void updateButtonAction(ActionEvent event){
        
        try {
            DBConnection.makeConnection();
            String customerName = name_txt.getText();
            
            for (int i = 0; i < customerList.size(); i++ ){
                Customer myCustomer = (Customer) customerList.get(i);
                if (customerName.equalsIgnoreCase(myCustomer.getName())){
                    
                    //Compare saved versus entered data.
                    if(myCustomer.getAddress_1().equals(ad1_txt.getText())){
                        //Do Nothing.
                    }else{
                        myCustomer.setAddress_1(ad1_txt.getText());
                    }
                    
                    if(ad2_txt.equals(NULL)){
                        //Do Nothing
                    }else{
                        myCustomer.setAddress_2(ad2_txt.getText());
                    }
                    
                    if(myCustomer.getCity().equals(city_txt.getText())){
                        //Do Nothing
                    }else{
                        try {
                            myCustomer.setCity(city_txt.getText());
                            ResultSet results;
                            String citiesQ = "SELECT * FROM U05EJ7.city \n" +
                                    "WHERE city = '" + city_txt.getText() +"';";
                            
                            Query.makeQuery(citiesQ);
                            results = Query.getResults();
                            
                            if(!results.next()){
                               
                                String allCities = "SELECT * FROM U05EJ7.city";
                                Query.makeQuery(allCities);
                                results = Query.getResults();
                                int counter = 1;
                                while (results.next()){
                                    counter += 1;
                                }
                                myCustomer.setCityID(counter);
                                //Create and insert new city
                                java.util.Date createdDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
                                Timestamp createDate = new java.sql.Timestamp(createdDate.getTime());
                                /*
                                )
                                ,' " + );";
                                */
                                String insertCityQ = "INSERT INTO `U05EJ7`.`city` (`cityId`, `city`, `countryId`, `createDate` , `createdBy`, `lastUpdate`, `lastUpdateBy`) \n" +
                                                     "VALUES ('" + counter + "','" + city_txt.getText() + "','" + myCustomer.getCountryID() + "','" + createDate + "','test','" + createDate + "','test');";
                                
                                Query.makeQuery(insertCityQ);
                                
                            } else { results.beforeFirst();
                            while(results.next()){
                                cityId = results.getInt("cityId");
                                myCustomer.setCityID(cityId);
                                
                            }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(UserFormsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    if(myCustomer.getZip().equals(zip_txt.getText())){
                        //Do Nothing
                    }else{
                        myCustomer.setZip(zip_txt.getText());
                    }
                    
                    if(myCustomer.getCountry().equals(country_box.getValue().toString())){
                        //Do Nothing
                    }else{
                        myCustomer.setCountry(country_box.getValue().toString());
                    }
                    
                    if(myCustomer.getPhone().equals(phone_txt.getText())){
                        //Do Nothing
                    }else{
                        myCustomer.setPhone(phone_txt.getText());
                    }
                    //Update Database
                    
                    String addressUpdate = "UPDATE address \n" +
                            "SET address = '" + myCustomer.getAddress_1() + "'," +
                            "address2 = '" + myCustomer.getAddress_2() + "'," +
                            "postalCode = '" + myCustomer.getZip() + "',"+
                            "phone = '" + myCustomer.getPhone() + "'," +
                            "cityId = '" + myCustomer.getCityID() + "'\n" +
                            "WHERE addressId = " + myCustomer.getAddressID()+ ";";
                    
                    Query.makeQuery(addressUpdate);
                    
                    Alert alert = new Alert(CONFIRMATION);
                    alert.setContentText("Customer: " + name_txt.getText() + " updated.");
                    alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> System.out.println("No name."));
                    
                } else if (i + 1 == customerList.size()){
                    Alert alert = new Alert(CONFIRMATION);
                    alert.setContentText("No customer with name: " + name_txt.getText() + " consider creating a new customer.");
                    alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> System.out.println("No name."));
                    
                } else{
                    //do nothing.
                }
            }
            DBConnection.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserFormsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserFormsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        name_txt.setText(null);
        ad1_txt.setText(null);
        ad2_txt.setText(null);
        city_txt.setText(null);
        zip_txt.setText(null);
        phone_txt.setText(null);
        country_box.setValue(null);
    }
    
    @FXML
    void mouseClicked(MouseEvent event){
        if (event.getClickCount() == 1)
      {
            customer =  customer_tbl.getSelectionModel().getSelectedItem();
            name_txt.setText(customer.getName());
            ad1_txt.setText(customer.getAddress_1());
            ad2_txt.setText(customer.getAddress_2());
            city_txt.setText(customer.getCity());
            zip_txt.setText(customer.getZip());
            phone_txt.setText(customer.getPhone());
            country_box.setValue(customer.getCountry());
            
      }
        
    }
    
    //Navigation Menu
    @FXML
    void appointmentForm(ActionEvent event) throws IOException{
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       customer_tbl.getItems().clear();
        for (int i = 0; i < customerList.size(); i++) {
                        Customer myCustomer = (Customer) customerList.get(i);
                        customer_tbl.getColumns().setAll(customer_column);
                        customer_column.setCellValueFactory(new PropertyValueFactory<>("name"));
                        customer_tbl.getItems().add(myCustomer);
        }
        country_box.getItems().addAll("US","UK","Canada","Norway");
        name_txt.setText(null);
            ad1_txt.setText(null);
            ad2_txt.setText(null);
            city_txt.setText(null);
            zip_txt.setText(null);
            phone_txt.setText(null);
            country_box.setValue(null);
    }    
    
}
