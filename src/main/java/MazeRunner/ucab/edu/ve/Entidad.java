package MazeRunner.ucab.edu.ve;

abstract class Entidad implements Comparable<Entidad> {
    char ascii;

    // Provide a default way to get the display character for an entity
    public char obtenerAscii() {
        return this.ascii;
    }

    public abstract void interact(Jugador player);

    @Override
    public int compareTo(Entidad otraEntidad) {
        return Character.compare(this.ascii, otraEntidad.ascii);
    }
}
