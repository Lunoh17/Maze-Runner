package MazeRunner.ucab.edu.ve;

import java.util.Stack;
import java.util.Scanner;

public class Jugador extends Entidad implements Movimiento {
    static Scanner scanner = new Scanner(System.in);
    private String correoElectronico;
    private String contrasenia;
    private Stack<Short> vidas;
    final static short MAX_VIDA = 10;
    public Celda celdaActual;

    public Jugador(String correoElectronico, String contrasenia) {
        this.correoElectronico = correoElectronico;
        this.contrasenia = contrasenia;
        this.vidas = new Stack<>();
        // Inicializar con 3 vidas
        for (int i = 0; i < 3; i++) {
            vidas.push(MAX_VIDA);
        }
        // optional internal ascii char (keeps ordering stable)
        this.ascii = '@';
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

    // Player overrides display char
    @Override
    public char obtenerAscii() {
        return '@';
    }

    @Override
    public void method() {

    }
}
