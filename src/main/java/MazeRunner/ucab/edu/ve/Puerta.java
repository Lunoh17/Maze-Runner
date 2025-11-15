package MazeRunner.ucab.edu.ve;

public class Puerta extends Entidad {
    public Puerta() {
        super();
        this.ascii = 'X';
    }

    /**
     * @param jugador
     */
    @Override
    public void interact(Jugador jugador) {
        if (jugador.tieneLlave()) {
            System.out.println("¡Has abierto la puerta y escapado del laberinto! ¡Felicidades!");
            jugador.escapar();
        } else {
            System.out.println("La puerta está cerrada. Necesitas una llave para abrirla.");
        }
    }
}
