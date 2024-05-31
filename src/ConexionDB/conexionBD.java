/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author helmu
 */
public class conexionBD {
    
    private String url = "jdbc:postgresql://bubble.db.elephantsql.com:5432/jqxkynmj";
    private Properties properties = new Properties();
    private static Connection conn = null;
    
    private conexionBD () {
        properties.setProperty("user", "jqxkynmj");
        properties.setProperty("password", "Je3n-kHRLLP6sR2FSkUYXP5bR_0Au2Bz");
        
        try {
            conn = DriverManager.getConnection(url, properties);
        } catch (SQLException ex) {
            Logger.getLogger(conexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Connection getPostgreSQLConnection() {
        return conn;
    }
    
    public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://bubble.db.elephantsql.com:5432/jqxkynmj";
    private static final String USER = "jqxkynmj"; // Cambia esto a tu usuario de MySQL
    private static final String PASSWORD = "Je3n-kHRLLP6sR2FSkUYXP5bR_0Au2Bz"; // Cambia esto a tu contraseña de MySQL

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }   
  }
}
