package MazeRunner.ucab.edu.ve;

public class Llave extends Entidad {
    public Llave() {
        this.ascii = 'K';
    }

    /**
     * @param jugador
     */
    @Override
    public void interact(Jugador jugador) {
        System.out.println("¡Has recogido la llave!, ya puedes ir a la salida.");
        jugador.recogerLlave();
    }
}
