/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Dustin
 */
public class Worden_Calendar extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

  
    public static void main(String[] args)  {
        
        try {
            launch(args);
        } catch (Exception ex) {
            Logger.getLogger(Worden_Calendar.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex);
        }
    }
    
}
