package MazeRunner.ucab.edu.ve;

import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Jugador extends Entidad implements Movimiento {
    final static short MAX_VIDA = 10;
    private static final Map<Character, Laberinto.DIR> DIRECTIONS_MAP = Map.of(
            'w', Laberinto.DIR.N,
            's', Laberinto.DIR.S,
            'a', Laberinto.DIR.W,
            'd', Laberinto.DIR.E
    );
    static Scanner scanner = new Scanner(System.in);
    private final String correoElectronico;
    private final String contrasenia;
    private final Stack<Short> vidas;
    public transient Celda celdaActual;
    private int puntos = 0;
    private int llaves = 0;

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

    public void recibirPuntos(int puntos) {
        this.puntos += puntos;
        System.out.println("Puntuación actual: " + this.puntos);
    }

    public void recogerLlave() {
        this.llaves++;
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
     * @param player
     */
    @Override
    public void interact(Jugador player) {

    }

    // Implement Movimiento.method(): input loop (W/A/S/D to move, Q to quit)
    @Override
    public int movimiento(Laberinto laberinto) {
        if (laberinto == null) {
            System.out.println("Jugador no tiene referencia al laberinto.");
            return -1;
        }

        System.out.println("Controls: W (up), A (left), S (down), D (right). Q to quit.");
        System.out.println("Vidas: " + vidas.size());
        System.out.print("Energia: ");
        if (!vidas.isEmpty()) {
            System.out.println(vidas.peek() + "/" + MAX_VIDA);
        } else {
            System.out.println("0/" + MAX_VIDA);
        }
        boolean movedSuccessfully = false;
        while (!movedSuccessfully) {
            System.out.print("Enter move (W/A/S/D) or Q to quit: ");
            String rawInput = scanner.nextLine();
            if (rawInput == null || rawInput.isEmpty()) {
                continue;
            }
            char inputChar = Character.toLowerCase(rawInput.charAt(0));
            if (inputChar == 'q') {
                System.out.println("Exiting player control.");
                laberinto.display();
                return 1;
            }
            movedSuccessfully = laberinto.movimientoEntidad(this, DIRECTIONS_MAP.get(inputChar));
            if (!movedSuccessfully) {
                System.out.println("Cannot move in that direction (wall or out of bounds).");
            }
        }
        laberinto.display();
        return 0;
    }

    // Static factory to reconstruct a Jugador from a Gson JsonObject
    public static Jugador fromJson(JsonObject obj) {
        String correo = obj.has("correoElectronico") ? obj.get("correoElectronico").getAsString() : "player@example.com";
        String pass = obj.has("contrasenia") ? obj.get("contrasenia").getAsString() : "password";
        Jugador j = new Jugador(correo, pass);
        if (obj.has("ascii")) {
            String s = obj.get("ascii").getAsString();
            if (s != null && !s.isEmpty()) {
                j.ascii = s.charAt(0);
            }
        }
        if (obj.has("vidas")) {
            JsonArray va = obj.getAsJsonArray("vidas");
            j.vidas.clear();
            for (int i = 0; i < va.size(); i++) {
                short v = (short) va.get(i).getAsInt();
                j.vidas.push(v);
            }
        }
        if (obj.has("puntos")) {
            j.puntos = obj.get("puntos").getAsInt();
        }
        if (obj.has("llaves")) {
            j.llaves = obj.get("llaves").getAsInt();
        }
        if (obj.has("posX") && obj.has("posY")) {
            int px = obj.get("posX").getAsInt();
            int py = obj.get("posY").getAsInt();
            j.setPosition(px, py);
        }
        return j;
    }

}
