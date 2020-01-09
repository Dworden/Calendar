package Model;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dustin
 */

//This class handles opening and closing of the Database Connection.
public class DBConnection {
    
    private static final String databaseName = "U05EJ7";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/" + databaseName;
    private static final String username = "U05EJ7";
    private static final String password = "53688477447";
    private static final String driver = "com.mysql.jdbc.Driver";
    public static Connection con;
    
    
    //Connects to database, throws exceptions if need be.
    public static void makeConnection()
    {// System.out.println("False Connection to database @DBCon.jv makeConnection(). For testing.");
        try {
            Class.forName(driver);
            
            con = (Connection) DriverManager.getConnection(DB_URL, username, password);
            System.out.println("Connected to database.");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex);
        }
    }
    
    
    //Closes Connection.
    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception
    {
       con.close();
      // System.out.println("False Database closed. @DBCon,jv closedConnection(). For testing");
    }
}
