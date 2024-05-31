/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * FXML Controller class
 *
 * @author helmu
 */
public class logincontroller implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfContraseña;
    @FXML
    private Button btlogin;
    @FXML
    private Label registerLabel;
    @FXML
    private Label lbestado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
         @FXML
    private void handleRegisterClick(ActionEvent event) {
        try {
        // Cargar el archivo FXML para la ventana de registro
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FormsRegistro.fxml"));
        Parent root = loader.load();

        // Crear una nueva escena con el archivo FXML cargado
        Scene scene = new Scene(root);

        // Crear una nueva ventana (Stage)
        Stage stage = new Stage();
        stage.setTitle("Registro de Usuario");
        stage.setScene(scene);
        stage.show();

        ((Node) (event.getSource())).getScene().getWindow().hide();

    } catch (IOException e) {
        e.printStackTrace();
        // Manejar la excepción en caso de que el archivo FXML no se pueda cargar
    }
 }
    
    
    @FXML
    private void btnOKClicked(ActionEvent event) {
    String usuario = tfUsuario.getText();
    String contraseña = tfContraseña.getText(); 

    if (validacion(usuario, contraseña)) {
        String rol = obtenerRol(usuario); // Método para obtener el rol del usuario desde la base de datos

        try {
            String vistaFXML;
            if (rol.equals("usuario")) {
                vistaFXML = "/vista/inicioUser.fxml";
            } else if (rol.equals("Administrador")) {
                vistaFXML = "/vista/inicioAdmin.fxml";
            } else {
                // Manejar el caso en el que el rol no esté definido correctamente
                return;
            }

            // Cargar el archivo FXML correspondiente
            FXMLLoader loader = new FXMLLoader(getClass().getResource(vistaFXML));
            Parent root = loader.load();

            // Obtener la ventana actual (stage)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Establecer la nueva escena con el archivo FXML cargado
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar la excepción en caso de que el archivo FXML no se pueda cargar
        }
    } else {
        lbestado.setText("Usuario o contraseña incorrectos");
    }
 }
    
        private Connection connect() {
        String url = "jdbc:postgresql://bubble.db.elephantsql.com:5432/jqxkynmj";
        String user = "jqxkynmj"; 
        String password = "Je3n-kHRLLP6sR2FSkUYXP5bR_0Au2Bz"; 
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    private String obtenerRol(String usuario) {
    String rol = null;
    String query = "SELECT rol FROM usuarios WHERE nombre_usuario = ?";
    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, usuario);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            rol = rs.getString("rol");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return rol;
}
    
    private boolean validacion(String usuario, String contraseña) {
    String query = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
    
    try (
        Connection conn = DriverManager.getConnection("jdbc:postgresql://bubble.db.elephantsql.com:5432/jqxkynmj", "jqxkynmj", "Je3n-kHRLLP6sR2FSkUYXP5bR_0Au2Bz");
        PreparedStatement pstmt = conn.prepareStatement(query)
    ) {
        pstmt.setString(1, usuario);
        pstmt.setString(2, contraseña);
        
        ResultSet rs = pstmt.executeQuery();
        
        return rs.next();
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
      }
   }
}
    

