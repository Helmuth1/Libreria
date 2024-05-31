
import controlador.libros;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author helmu
 */
public class ordenamientolibros {
        public void ordenarPorTitulo(List<libros> Libros) {
        Collections.sort(Libros, Comparator.comparing(libros::getTitulo));
    }
}
