/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author helmu
 */
public class HistorialAdminController implements Initializable {
    
    @FXML
    private TableView<prestamos> tablelibros;
    @FXML
    private TableColumn<prestamos, String> colUsuario;
    @FXML
    private TableColumn<prestamos, String> colISBN;
    @FXML
    private TableColumn<prestamos, String> colprestamo;
    @FXML
    private TableColumn<prestamos, String> coldevolver;
    @FXML
    private TableColumn<prestamos, String> coldevolucion;
    @FXML
    private TextField tfFiltrar;
    @FXML
    private Button btnRegresar;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("Usuario"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("libro_isbn"));
        colprestamo.setCellValueFactory(new PropertyValueFactory<>("fecha_prestamo"));
        coldevolver.setCellValueFactory(new PropertyValueFactory<>("fecha_devolucion"));
        coldevolucion.setCellValueFactory(new PropertyValueFactory<>("fecha_real_devolucion"));

        MostrarDatos();
        
        tfFiltrar.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabla(newValue));
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
    
        private void MostrarDatos() {
        String sql = "SELECT usuario, libro_isbn, fecha_prestamo, fecha_devolucion, fecha_real_devolucion FROM prestamos";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            listaPrestamos.clear();

            while (rs.next()) {
                listaPrestamos.add(new prestamos(
                    rs.getString("usuario"),
                    rs.getString("libro_isbn"),
                    rs.getDate("fecha_prestamo"),
                    rs.getDate("fecha_devolucion"),
                    rs.getDate("fecha_real_devolucion")
                ));
            }

            tablelibros.setItems(listaPrestamos);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
        private void filtrarTabla(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tablelibros.setItems(listaPrestamos);
            return;
        }

        ObservableList<prestamos> prestamosFiltrados = FXCollections.observableArrayList();

        for (prestamos prestamo : listaPrestamos) {
            if (prestamo.getUsuario().toLowerCase().contains(filtro.toLowerCase()) ||
                prestamo.getLibro_isbn().toLowerCase().contains(filtro.toLowerCase())) {
                prestamosFiltrados.add(prestamo);
            }
        }

        tablelibros.setItems(prestamosFiltrados);
    }
    
    private ObservableList<prestamos> listaPrestamos = FXCollections.observableArrayList();
    
    @FXML
    private void btnBackClicked(ActionEvent event) {
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/inicioAdmin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
