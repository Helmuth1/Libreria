package controlador;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
/**
 * FXML Controller class
 *
 * @author helmu
 */



public class RegistrolibrosController implements Initializable {


    @FXML
    private TableView<libros> tablelibros;
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
    private TextField tfISBN;
    @FXML
    private TextField tfTitulo;
    @FXML
    private TextField tfAutor;
    @FXML
    private TextField tfDisponibilidad;
    @FXML
    private TextField tfpublicacion;
    @FXML
    private TextField tfEditorial;
    @FXML
    private Button btAgregar;
    @FXML
    private Button btModificar;
    @FXML
    private Button btEliminar;
    @FXML
    private Button btnRegresar;

    private ObservableList<libros> librosList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnopublicacion.setCellValueFactory(new PropertyValueFactory<>("AnoPublicacion"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<>("Disponibilidad"));
        
        tablelibros.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<libros>() {
        @Override
        public void changed(ObservableValue<? extends libros> observable, libros oldValue, libros newValue) {
            if (newValue != null) {
                llenarCampos(newValue);
            }
        }
    });
        
        MostrarDatos();
    }    

    @FXML
    private void btnBackClicked(ActionEvent event) {
            try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/inicioAdmin.fxml"));
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
    private void btnOkClicked(ActionEvent event) throws ParseException {
        try {
            String isbn = tfISBN.getText();
            String titulo = tfTitulo.getText();
            String autor = tfAutor.getText();
            String editorial = tfEditorial.getText();
            int disponibilidad = Integer.parseInt(tfDisponibilidad.getText());
            
            String fechaTexto = tfpublicacion.getText();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            Date anoPublicacion = new Date(formatoFecha.parse(fechaTexto).getTime());

            String query = "INSERT INTO libros (isbn, titulo, autor, ano_publicacion, editorial, cantidad_disponible) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, isbn);
                pstmt.setString(2, titulo);
                pstmt.setString(3, autor);
                pstmt.setDate(4, anoPublicacion);
                pstmt.setString(5, editorial);
                pstmt.setInt(6, disponibilidad);
                pstmt.executeUpdate();

                librosList.add(new libros(isbn, titulo, autor, anoPublicacion, editorial, disponibilidad));
                limpiarCampos();
                MostrarDatos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese valores numéricos válidos.");
        }
    }
    
     private void limpiarCampos() {
        tfISBN.clear();
        tfTitulo.clear();
        tfAutor.clear();
        tfpublicacion.clear();
        tfEditorial.clear();
        tfDisponibilidad.clear();
    }

     private void llenarCampos(libros libro) {
        tfISBN.setText(libro.getIsbn());
        tfTitulo.setText(libro.getTitulo());
        tfAutor.setText(libro.getAutor());
        tfpublicacion.setText(libro.getAnoPublicacion().toString()); 
        tfEditorial.setText(libro.getEditorial());
        tfDisponibilidad.setText(String.valueOf(libro.getDisponibilidad()));
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
        tablelibros.setItems(libros);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    }
     
         @FXML
    private void modificarLibro(ActionEvent event) {
    String isbn = tfISBN.getText();
    String titulo = tfTitulo.getText();
    String autor = tfAutor.getText();
    String fechaTexto = tfpublicacion.getText();
    String editorial = tfEditorial.getText();
    String cantidadDisponibleStr = tfDisponibilidad.getText();

    java.sql.Date anoPublicacion;
    int cantidadDisponible;
    try {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsedDate = formatoFecha.parse(fechaTexto);
        anoPublicacion = new java.sql.Date(parsedDate.getTime());
        cantidadDisponible = Integer.parseInt(cantidadDisponibleStr);
    } catch (NumberFormatException | ParseException e) {
        // Mostrar mensaje de error al usuario
        System.out.println("Por favor, ingrese valores válidos.");
        return;
    }

    String query = "UPDATE libros SET titulo = ?, autor = ?, ano_publicacion = ?, editorial = ?, cantidad_disponible = ? WHERE isbn = ?";

    try (Connection conn = DriverManager.getConnection("jdbc:postgresql://bubble.db.elephantsql.com:5432/jqxkynmj", "jqxkynmj", "Je3n-kHRLLP6sR2FSkUYXP5bR_0Au2Bz");
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, titulo);
        pstmt.setString(2, autor);
        pstmt.setDate(3, anoPublicacion);
        pstmt.setString(4, editorial);
        pstmt.setInt(5, cantidadDisponible);
        pstmt.setString(6, isbn);
        pstmt.executeUpdate();

        MostrarDatos();
        limpiarCampos();
    } catch (SQLException e) {
        e.printStackTrace();
    }
  }

    @FXML
    private void eliminarlibro(ActionEvent event) {
    String ISBN = tfISBN.getText();

    String query = "DELETE FROM libros WHERE isbn = ?";

    try (Connection conn = DriverManager.getConnection("jdbc:postgresql://bubble.db.elephantsql.com:5432/jqxkynmj", "jqxkynmj", "Je3n-kHRLLP6sR2FSkUYXP5bR_0Au2Bz");
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, ISBN);
        pstmt.executeUpdate();

        MostrarDatos();
        limpiarCampos();
    } catch (SQLException e) {
        e.printStackTrace();
    }
  
  }
    
}
