/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author helmu
 */
public class FormsRegistroController implements Initializable {

    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfUsuario;
    @FXML
    private Button btcrear;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfnombre;
    @FXML
    private PasswordField tfContraseña;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnOKClicked(ActionEvent event) {
            // Obtener los valores de los campos de texto
    String usuario = tfUsuario.getText();
    String nombre = tfnombre.getText();
    String direccion = tfDireccion.getText();
    String telefono = tfTelefono.getText();
    String id = tfID.getText();
    String contrasena = tfContraseña.getText();

    // Construir la consulta SQL para insertar los datos en la tabla de usuarios
    String query = "INSERT INTO usuarios (nombre_usuario, nombre, direccion, telefono, numero_identificacion, contrasena) VALUES (?, ?, ?, ?, ?, ?)";

    try (
        // Establecer la conexión a la base de datos
        Connection conn = DriverManager.getConnection("jdbc:postgresql://bubble.db.elephantsql.com:5432/jqxkynmj", "jqxkynmj", "Je3n-kHRLLP6sR2FSkUYXP5bR_0Au2Bz");
        PreparedStatement pstmt = conn.prepareStatement(query)
    ) {
        // Establecer los valores de los parámetros en la consulta SQL
        pstmt.setString(1, usuario);
        pstmt.setString(2, nombre);
        pstmt.setString(3, direccion);
        pstmt.setString(4, telefono);
        pstmt.setString(5, id);
        pstmt.setString(6, contrasena);

        // Ejecutar la consulta para insertar los datos en la tabla
        pstmt.executeUpdate();

        // Opcional: mostrar un mensaje de éxito
        System.out.println("Datos insertados correctamente en la base de datos.");
    } catch (SQLException e) {
        // Manejar cualquier excepción de SQL
        e.printStackTrace();
    }
    try {
        // Cargar la nueva ventana
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/login.fxml"));
        Parent root = loader.load();

        // Obtener la escena actual y establecer la nueva escena
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        // Manejar cualquier excepción de E/S
        e.printStackTrace();
    }
    }
    
    
}
