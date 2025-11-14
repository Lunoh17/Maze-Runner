package MazeRunner.ucab.edu.ve;

public class Trampa extends Entidad {
    short danio = 10;

    @Override
    public void interact(Jugador jugador) {
        System.out.println("¡Has caído en una trampa! Pierdes " + danio + " puntos de vida.");
        jugador.recibirDanio(danio);
    }
}
