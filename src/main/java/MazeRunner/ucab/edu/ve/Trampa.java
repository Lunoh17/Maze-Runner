package MazeRunner.ucab.edu.ve;

public class Trampa extends Entidad {
    short danio = 1;
    public void hacerDanio(Jugador jugador) {
        System.out.println("¡Has caído en una trampa! Pierdes 10 puntos de vida.");
        jugador.recibirDanio(danio);
    }
}
