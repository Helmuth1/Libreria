/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.Date;

/**
 *
 * @author helmu
 */
public class prestamos {
    
    private String usuario;
    private String libro_isbn;
    private Date fecha_prestamo;
    private Date fecha_devolucion;
    private Date fecha_real_devolucion;

    public prestamos(String usuario, String libro_isbn, Date fecha_prestamo, Date fecha_devolucion, Date fecha_real_devolucion) {
        this.usuario = usuario;
        this.libro_isbn = libro_isbn;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_devolucion = fecha_devolucion;
        this.fecha_real_devolucion = fecha_real_devolucion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLibro_isbn() {
        return libro_isbn;
    }

    public void setLibro_isbn(String libro_isbn) {
        this.libro_isbn = libro_isbn;
    }

    public Date getFecha_prestamo() {
        return fecha_prestamo;
    }

    public void setFecha_prestamo(Date fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }

    public Date getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(Date fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public Date getFecha_real_devolucion() {
        return fecha_real_devolucion;
    }

    public void setFecha_real_devolucion(Date fecha_real_devolucion) {
        this.fecha_real_devolucion = fecha_real_devolucion;
    }

    
}
