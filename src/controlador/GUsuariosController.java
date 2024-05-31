/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author helmu
 */
public class GUsuariosController implements Initializable {

    @FXML
    private TableView<usuarios> tableusuarios;
    @FXML
    private TextField tfIdentificacion;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfTelefono;
    @FXML
    private Button btAgregar;
    @FXML
    private Button btmodificar;
    @FXML
    private Button btEliminar;
    @FXML
    private Button btBuscar;
    @FXML
    private Button btRegresar;
    @FXML
    private TextField tfRol;
    @FXML
    private TextField tfcontraseña;
    @FXML
    private TextField tfUsuario;    
    @FXML
    private TableColumn<usuarios, String> colID;
    @FXML
    private TableColumn<usuarios, String> colNombre;
    @FXML
    private TableColumn<usuarios, String> colidentificacion;
    @FXML
    private TableColumn<usuarios, String> colDireccion;
    @FXML
    private TableColumn<usuarios, String> colTelefono;
    @FXML
    private TableColumn<usuarios, String> colRol;
    @FXML
    private TableColumn<usuarios, String> colUsuario;
    @FXML
    private TableColumn<usuarios, String> colContrasena;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colidentificacion.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        colContrasena.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        
        tableusuarios.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<usuarios>() {
        @Override
        public void changed(ObservableValue<? extends usuarios> observable, usuarios oldValue, usuarios newValue) {
            if (newValue != null) {
                llenarCampos(newValue);
            }
        }
    });
    // Cargar los datos desde la base de datos y llenar la TableView
    Mostrardatos();
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
    

    private void Mostrardatos() {
    String sql = "SELECT id, nombre_usuario, nombre, direccion, telefono, numero_identificacion, contrasena, rol FROM usuarios";
    
    try (Connection conn = this.connect();
         Statement stmt  = conn.createStatement();
         ResultSet rs    = stmt.executeQuery(sql)){
        
        ObservableList<usuarios> userList = FXCollections.observableArrayList();
        
        while (rs.next()) {
            userList.add(new usuarios(
                rs.getInt("id"),
                rs.getString("nombre_usuario"),
                rs.getString("nombre"),
                rs.getString("direccion"),
                rs.getString("telefono"),
                rs.getString("numero_identificacion"),
                rs.getString("contrasena"),
                rs.getString("rol")
                
                
            ));
        }
        tableusuarios.setItems(userList);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}

    @FXML
private void agregarUsuario(ActionEvent event) {
    String sql = "INSERT INTO usuarios(nombre_usuario, nombre, direccion, telefono, numero_identificacion, contrasena, rol) VALUES(?,?,?,?,?,?,?)";
    
    try (Connection conn = this.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, tfUsuario.getText()); 
        pstmt.setString(2, tfNombre.getText()); 
        pstmt.setString(3, tfDireccion.getText()); 
        pstmt.setString(4, tfTelefono.getText()); 
        pstmt.setString(5, tfIdentificacion.getText()); 
        pstmt.setString(6, tfcontraseña.getText()); 
        pstmt.setString(7, tfRol.getText()); 
        pstmt.executeUpdate();
        limpiarCampos();
        Mostrardatos();
        
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
  }

    public void limpiarCampos() {
        tfUsuario.clear();
        tfNombre.clear();
        tfDireccion.clear();
        tfTelefono.clear();
        tfIdentificacion.clear();
        tfcontraseña.clear();
        tfRol.clear();
    }
    
    private void llenarCampos(usuarios usuario) {
        tfUsuario.setText(usuario.getUsuario());
        tfNombre.setText(usuario.getNombre());
        tfDireccion.setText(usuario.getDireccion());
        tfTelefono.setText(usuario.getTelefono()); 
        tfIdentificacion.setText(usuario.getIdentificacion());
        tfcontraseña.setText(usuario.getContraseña());
        tfRol.setText(usuario.getRol());
    
}
    
             @FXML
    private void modificarUsuario(ActionEvent event) {
    String Usuario = tfUsuario.getText();
    String nombre = tfNombre.getText();
    String direccion = tfDireccion.getText();
    String telefono = tfTelefono.getText();
    String identificacion = tfIdentificacion.getText();
    String contraseña = tfcontraseña.getText();
    String rol = tfRol.getText();
    

    String query = "UPDATE usuarios SET nombre = ?, direccion = ?, telefono = ?, numero_identificacion = ?, contrasena = ?, rol = ? WHERE nombre_usuario = ?";

    try (Connection conn = DriverManager.getConnection("jdbc:postgresql://bubble.db.elephantsql.com:5432/jqxkynmj", "jqxkynmj", "Je3n-kHRLLP6sR2FSkUYXP5bR_0Au2Bz");
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, nombre);
        pstmt.setString(2, direccion);
        pstmt.setString(3, telefono);
        pstmt.setString(4, identificacion);
        pstmt.setString(5, contraseña);
        pstmt.setString(6, rol);
        pstmt.setString(7, Usuario);

        pstmt.executeUpdate();

        Mostrardatos();
        limpiarCampos();
    } catch (SQLException e) {
        e.printStackTrace();
    }
  }
   
        @FXML
    private void eliminarUsuario(ActionEvent event) {
    String nombre_usuario = tfUsuario.getText();

    String query = "DELETE FROM usuarios WHERE nombre_usuario = ?";

    try (Connection conn = DriverManager.getConnection("jdbc:postgresql://bubble.db.elephantsql.com:5432/jqxkynmj", "jqxkynmj", "Je3n-kHRLLP6sR2FSkUYXP5bR_0Au2Bz");
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, nombre_usuario);
        pstmt.executeUpdate();

        Mostrardatos();
        limpiarCampos();
    } catch (SQLException e) {
        e.printStackTrace();
    }
  
  }
    
}
