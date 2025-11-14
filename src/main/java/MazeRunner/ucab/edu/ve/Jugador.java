package MazeRunner.ucab.edu.ve;

import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Jugador extends Entidad implements Movimiento {
    final static short MAX_VIDA = 10;
    private static final Map<Character, Laberinto.DIR> DIRECTIONS_MAP = Map.of(
            'w', Laberinto.DIR.N,
            's', Laberinto.DIR.S,
            'a', Laberinto.DIR.W,
            'd', Laberinto.DIR.E
    );
    static Scanner scanner = new Scanner(System.in);
    public Celda celdaActual;
    private final String correoElectronico;
    private final String contrasenia;
    private final Stack<Short> vidas;
    private Laberinto laberinto; // reference to the maze this player belongs to
    private int posX = 0;
    private int posY = 0;
    private int score = 0;

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

    // New constructor that receives the Laberinto reference
    public Jugador(String correoElectronico, String contrasenia, Laberinto laberinto) {
        this(correoElectronico, contrasenia);
        this.laberinto = laberinto;
    }

    public void setPosition(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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

    /**
     * @param player
     */
    @Override
    public void interact(Jugador player) {

    }

    // Implement Movimiento.method(): input loop (W/A/S/D to move, Q to quit)
    @Override
    public void method() {
        if (laberinto == null) {
            System.out.println("Jugador no tiene referencia al laberinto.");
            return;
        }

        System.out.println("Controls: W (up), A (left), S (down), D (right). Q to quit.");
        laberinto.display();

        while (true) {
            System.out.print("Enter move (W/A/S/D) or Q to quit: ");
            String rawInput = scanner.nextLine();
            if (rawInput == null || rawInput.isEmpty()) {
                continue;
            }
            char inputChar = Character.toLowerCase(rawInput.charAt(0));
            if (inputChar == 'q') {
                System.out.println("Exiting player control.");
                break;
            }
            boolean movedSuccessfully = laberinto.movimientoJugador(this, DIRECTIONS_MAP.get(inputChar));
            if (!movedSuccessfully) {
                System.out.println("Cannot move in that direction (wall or out of bounds).");
            } else {
                laberinto.display();
            }
        }
    }

}
