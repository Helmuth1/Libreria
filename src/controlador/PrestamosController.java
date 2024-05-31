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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author helmu
 */
public class PrestamosController implements Initializable {

    @FXML
    private TableView<libros> tablelibros1;
    @FXML
    private TableColumn<libros, String> colISBN;
    @FXML
    private TableColumn<libros, String> colTitulo;
    @FXML
    private TableColumn<libros, String> colAutor;
    @FXML
    private TableColumn<libros, Integer> colAnopublicacion;
    @FXML
    private TableColumn<libros, String> colEditorial;
    @FXML
    private TableColumn<libros, Integer> colDisponibilidad;
    @FXML
    private Button prestarLibro;
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

    private void limpiarCampos() {
        tfUsuarioId.clear();
        tfISBN.clear();
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

    private boolean esLibroDisponible(String isbn) {
    String query = "SELECT cantidad_disponible FROM libros WHERE isbn = ?";
    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, isbn);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int cantidad = rs.getInt("cantidad_disponible");
            System.out.println("Cantidad disponible de ISBN " + isbn + ": " + cantidad);
            return cantidad > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
private boolean tienePrestamosPendientes(String usuario) {
    String query = "SELECT * FROM prestamos WHERE usuario = ? AND (fecha_real_devolucion IS NULL OR fecha_real_devolucion > CURRENT_DATE)";
    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, usuario);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

private boolean tieneLibroPrestadoHoy(String usuario, String isbn) {
    String query = "SELECT COUNT(*) AS total FROM prestamos WHERE usuario = ? AND libro_isbn = ? AND fecha_prestamo = CURRENT_DATE AND fecha_devolucion IS NULL";
    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, usuario);
        pstmt.setString(2, isbn);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int total = rs.getInt("total");
            System.out.println("Libros prestados hoy para usuario " + usuario + " con ISBN " + isbn + ": " + total);
            return total > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

@FXML
private void prestarLibro(ActionEvent event) {
    String usuario = tfUsuarioId.getText();
    String isbn = tfISBN.getText();

    if (!esLibroDisponible(isbn)) {
        mostrarMensaje("Error", "El libro no está disponible.");
        return;
    }

    if (tienePrestamosPendientes(usuario)) {
        mostrarMensaje("Error", "El usuario tiene préstamos pendientes.");
        return;
    }

    if (tieneLibroPrestadoHoy(usuario, isbn)) {
        mostrarMensaje("Error", "El usuario ya tiene el libro en préstamo hoy.");
        return;
    }
    
    // Calcular la fecha de devolución
    LocalDate fechaPrestamo = LocalDate.now();
    LocalDate fechaDevolucion = fechaPrestamo.plus(14, ChronoUnit.DAYS);

    String queryPrestamo = "INSERT INTO prestamos (usuario, libro_isbn, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?)";
    String queryActualizarLibro = "UPDATE libros SET cantidad_disponible = cantidad_disponible - 1 WHERE isbn = ?";

    try (Connection conn = connect();
         PreparedStatement pstmtPrestamo = conn.prepareStatement(queryPrestamo);
         PreparedStatement pstmtActualizarLibro = conn.prepareStatement(queryActualizarLibro)) {

        // Registrar el préstamo con la fecha de devolución calculada
        pstmtPrestamo.setString(1, usuario);
        pstmtPrestamo.setString(2, isbn);
        pstmtPrestamo.setDate(3, java.sql.Date.valueOf(fechaPrestamo));
        pstmtPrestamo.setDate(4, java.sql.Date.valueOf(fechaDevolucion));
        pstmtPrestamo.executeUpdate();

        // Actualizar la cantidad disponible del libro
        pstmtActualizarLibro.setString(1, isbn);
        pstmtActualizarLibro.executeUpdate();

        MostrarDatos();
        limpiarCampos();
        mostrarMensaje("Éxito", "Préstamo registrado exitosamente. Fecha de devolución: " + fechaDevolucion);
    } catch (SQLException e) {
        e.printStackTrace();
        mostrarMensaje("Error", "Ocurrió un error al registrar el préstamo.");
    }
}

    private void mostrarMensaje(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
    }
}

