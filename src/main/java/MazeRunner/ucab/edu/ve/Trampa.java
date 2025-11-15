package MazeRunner.ucab.edu.ve;

public class Trampa extends Entidad {
    short danio = 1;
    public Trampa(){
        super();
        this.ascii = 'T';
    }
    @Override
    public void interact(Jugador jugador) {
        System.out.println("¡Has caído en una trampa! Pierdes " + danio + " punto de vida.");
        jugador.recibirDanio(danio);
    }
}
