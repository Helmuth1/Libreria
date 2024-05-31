
import controlador.usuarios;
import controlador.libros;
import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author helmu
 */
public class biblioteca {
        private List<libros> libros;
    private List<usuarios> usuarios;

    public biblioteca() {
        libros = new ArrayList<>();
        usuarios = new ArrayList<>();
    }

    public void agregarLibro(libros libro) {
        libros.add(libro);
    }

    public void agregarUsuario(usuarios usuario) {
        usuarios.add(usuario);
    }

    // Métodos para obtener, actualizar y eliminar libros y usuarios
}
