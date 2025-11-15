package MazeRunner.ucab.edu.ve;

public class Cristal extends Entidad {
    private int puntuacion = 50;

    public Cristal() {
        this.ascii = 'C';
    }

    /**
     * @param jugador
     */
    @Override
    public void interact(Jugador jugador) {
        System.out.println("¡Cristal recogido! Obtienes " + puntuacion + " puntos.");
        jugador.recibirPuntos(puntuacion);
    }
}
