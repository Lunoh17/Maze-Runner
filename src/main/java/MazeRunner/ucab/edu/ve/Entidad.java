package MazeRunner.ucab.edu.ve;

public class Entidad implements Comparable<Entidad> {
    char ascii;

    // Provide a default way to get the display character for an entity
    public char obtenerAscii() {
        return this.ascii;
    }

    @Override
    public int compareTo(Entidad otraEntidad) {
        return Character.compare(this.ascii, otraEntidad.ascii);
    }
}
