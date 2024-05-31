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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author helmu
 */
public class InicioController implements Initializable {

    @FXML
    private Button btnRegistro;
    @FXML
    private Button btnGestion;
    @FXML
    private Button btnHistorial;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnPrestamos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    public void btnOkClicked(ActionEvent event) {
        // Implementar la lógica para manejar los botones Registro, Gestión y Historial
        Button sourceButton = (Button) event.getSource();
        String buttonText = sourceButton.getText();
    
        // Ejemplo de carga de diferentes escenas según el botón
        String fxmlFile = "";
        if (sourceButton == btnRegistro) {
            fxmlFile = "/vista/registrolibros.fxml";
        } else if (sourceButton == btnGestion) {
            fxmlFile = "/vista/gUsuarios.fxml";
        } else if (sourceButton == btnHistorial) {
            fxmlFile = "/vista/historial.fxml";
        } 
        

        try {
            // Cargar el archivo FXML correspondiente
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
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
    }
    
    @FXML
    public void btnBackClicked(ActionEvent event) {
                 try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/login.fxml"));
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
    }
    
}
