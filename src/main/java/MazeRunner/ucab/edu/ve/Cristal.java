package MazeRunner.ucab.edu.ve;

public class Cristal extends Entidad {
    private final int puntuacion = 50;

    public Cristal() {
        super();
        this.ascii = 'C';
    }

    /**
     * @param jugador
     */
    @Override
    public void interact(Jugador jugador) {
        System.out.println("¡Cristal recogido! Obtienes " + puntuacion + " puntos.");
        jugador.recibirPuntos(puntuacion);
        jugador.celdaActual.removeEntidad(this);
    }
}
