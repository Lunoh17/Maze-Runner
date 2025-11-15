package MazeRunner.ucab.edu.ve;

import java.util.Set;

public class Celda {
    private final Set<Entidad> contenido = new java.util.TreeSet<>();
    public int valor = 0;

    public int cantidadEntidades() {
        return contenido.size();
    }

    public char obtenerAscii() {
        if (contenido.isEmpty()) {
            return ' ';
        } else if (contenido.size() > 1) {
            // Retorna un carácter especial si hay múltiples entidades
            return (Integer.toString(cantidadEntidades()).charAt(0));
        } else {
            // Use the entity's own obtenerAscii method
            return contenido.iterator().next().obtenerAscii();
        }
    }

    // Add / remove entities from the cell
    public void addEntidad(Entidad e) {
        if (e != null) {
            contenido.add(e);
        }
    }

    public void removeEntidad(Entidad e) {
        if (e != null) {
            contenido.remove(e);
        }
    }

    public java.util.Set<Entidad> obtenerContenido() {
        return java.util.Collections.unmodifiableSet(contenido);
    }
}
