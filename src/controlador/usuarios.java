package controlador;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author helmu
 */
public class usuarios {
    
    private int id;
    private String Usuario;
    private String nombre;
    private String direccion;
    private String telefono;
    private String identificacion;
    private String contraseña;
    private String rol;
    
    

    // Constructor
    public usuarios(int id, String Usuario, String nombre, String direccion, String telefono, String identificacion, String contraseña, String rol) {
        this.id = id;
        this.Usuario = new String(Usuario);
        this.nombre = new String(nombre);
        this.direccion = new String(direccion);
        this.telefono = new String(telefono);
        this.identificacion = new String(identificacion);
        this.contraseña = new String(contraseña);
        this.rol = new String(rol);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    
        
}

    
