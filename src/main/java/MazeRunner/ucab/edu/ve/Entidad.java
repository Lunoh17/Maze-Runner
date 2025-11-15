package MazeRunner.ucab.edu.ve;

public abstract class Entidad implements Comparable<Entidad> {
    protected char ascii;
    protected int posX = 0;
    protected int posY = 0;

    public void setPosition(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

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
