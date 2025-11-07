package MazeRunner.ucab.edu.ve;

import java.util.Stack;

public class Jugador extends Entidad implements Movimiento {
    private String nombre = "pelagato";
    private String contrasenia;
    private Stack<Short> vidas;
    final static short MAX_VIDA = 10;
    public Celda celdaActual;

    public Jugador(String contrasenia) {
        this.contrasenia = contrasenia;
        this.vidas = new Stack<>();
        // Inicializar con 3 vidas
        for (int i = 0; i < 3; i++) {
            vidas.push(MAX_VIDA);
        }
    }

    public void recibirDanio(short dano) {
        if (!vidas.isEmpty()) {
            short vidaActual = vidas.pop();
            vidaActual -= dano;
            if (vidaActual > 0) {
                vidas.push(vidaActual);
            } else {
                System.out.println("¡Has perdido una vida!");
            }
        } else {
            System.out.println("¡No te quedan vidas!");
        }
    }
    /**
     *
     */
    @Override
    public void method() {

    }
}
