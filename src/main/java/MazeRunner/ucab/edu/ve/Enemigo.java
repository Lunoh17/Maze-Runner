package MazeRunner.ucab.edu.ve;

public class Enemigo extends Trampa implements Movimiento {
    public Enemigo() {
        super();
        this.ascii = 'E';
        this.danio = 2; // Enemigos hacen más daño que trampas normales
    }

    @Override
    public int movimiento(Laberinto laberinto) {

        if (laberinto == null) {
            System.out.println("Enemigo no tiene referencia al laberinto.");
            return -1;
        }

        boolean movedSuccessfully = laberinto.movimientoEntidad(this, Laberinto.DIR.values()[(int) (Math.random() * 4)]);
        if (!movedSuccessfully) {
            System.out.println("El Enemigo se metio tremendo coniazo contra la pared (wall or out of bounds).");
        }
        laberinto.display();

        return 0;
    }
}
