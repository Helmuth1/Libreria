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
import java.sql.PreparedStatement;
import java.time.LocalDate;
import javafx.scene.control.Alert;


/**
 * FXML Controller class
 *
 * @author helmu
 */
public class DevolucionController implements Initializable {

    @FXML
    private TableView<libros> tablelibros1;
    @FXML
    private TableColumn<libros, String> colISBN;
    @FXML
    private TableColumn<libros, String> colTitulo;
    @FXML
    private TableColumn<libros, String> colAutor;
    @FXML
    private TableColumn<libros,  Date> colAnopublicacion;
    @FXML
    private TableColumn<libros, String> colEditorial;
    @FXML
    private TableColumn<libros, Integer> colDisponibilidad;
    @FXML
    private Button devolverlibro;
    @FXML
    private TextField tfUsuarioId;
    @FXML
    private TextField tfISBN;
    @FXML
    private Button btnRegresar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnopublicacion.setCellValueFactory(new PropertyValueFactory<>("anoPublicacion"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<>("disponibilidad"));

        MostrarDatos();
    }    

        private void MostrarDatos() {
        String sql = "SELECT isbn, titulo, autor, ano_publicacion, editorial, cantidad_disponible FROM libros";
    
    try (Connection conn = this.connect();
         Statement stmt  = conn.createStatement();
         ResultSet rs    = stmt.executeQuery(sql)){
        
        ObservableList<libros> libros = FXCollections.observableArrayList();
        
        while (rs.next()) {
            libros.add(new libros(
                rs.getString("ISBN"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getDate("ano_Publicacion"),
                rs.getString("editorial"),
                rs.getInt("cantidad_disponible")
 
            ));
        }
        tablelibros1.setItems(libros);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
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
    
        @FXML
    private void devolver(ActionEvent event) {
    String usuario = tfUsuarioId.getText();
    String isbn = tfISBN.getText();

    String queryVerificarPrestamo = "SELECT * FROM prestamos WHERE usuario = ? AND libro_isbn = ? AND fecha_real_devolucion IS NULL AND fecha_devolucion >= ?";
    String queryActualizarPrestamo = "UPDATE prestamos SET fecha_real_devolucion = ? WHERE usuario = ? AND libro_isbn = ? AND fecha_real_devolucion IS NULL";
    String queryActualizarLibro = "UPDATE libros SET cantidad_disponible = cantidad_disponible + 1 WHERE isbn = ?";

    try (Connection conn = connect();
         PreparedStatement pstmtVerificarPrestamo = conn.prepareStatement(queryVerificarPrestamo);
         PreparedStatement pstmtActualizarPrestamo = conn.prepareStatement(queryActualizarPrestamo);
         PreparedStatement pstmtActualizarLibro = conn.prepareStatement(queryActualizarLibro)) {

        // Verificar si el préstamo existe y no ha sido devuelto aún
        pstmtVerificarPrestamo.setString(1, usuario);
        pstmtVerificarPrestamo.setString(2, isbn);
        pstmtVerificarPrestamo.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
        ResultSet rs = pstmtVerificarPrestamo.executeQuery();

        if (!rs.next()) {
            mostrarMensaje("Error", "No hay préstamos pendientes para este libro y usuario.");
            return;
        }

        // Actualizar la fecha real de devolución del préstamo
        pstmtActualizarPrestamo.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
        pstmtActualizarPrestamo.setString(2, usuario);
        pstmtActualizarPrestamo.setString(3, isbn);
        pstmtActualizarPrestamo.executeUpdate();

        // Actualizar la cantidad disponible del libro
        pstmtActualizarLibro.setString(1, isbn);
        pstmtActualizarLibro.executeUpdate();

        MostrarDatos();
        mostrarMensaje("Éxito", "Libro devuelto exitosamente.");
    } catch (SQLException e) {
        e.printStackTrace();
        mostrarMensaje("Error", "Ocurrió un error al devolver el libro.");
    }
}
    
        private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    

    @FXML
    private void btnBackClicked(ActionEvent event) {
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/inicioUser.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar la excepción en caso de que el archivo FXML no se pueda cargar
        }
    }
    
}
