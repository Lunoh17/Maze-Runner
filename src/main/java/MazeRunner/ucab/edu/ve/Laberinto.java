package MazeRunner.ucab.edu.ve;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class Laberinto {
    private static final int MAX_DIM = 50;
    private static final int MIN_DIM = 1;
    private final int x;
    private final int y;
    private final Celda[][] maze;
    // store the player so it persists and can be used to start the input loop
    private final Jugador jugador;
    private Vector<Entidad> entidades = new Vector<>();

    public Laberinto(int size) {
        this(clamp(size), clamp(size));
    }

    public Laberinto(int x, int y) {
        this.x = Math.max(MIN_DIM, Math.min(MAX_DIM, x));
        this.y = Math.max(MIN_DIM, Math.min(MAX_DIM, y));
        maze = new Celda[this.x][this.y];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = new Celda();
            }
        }
        generateMaze(0, 0);
        final int nPeligro = Math.toIntExact(Math.round((double) (this.x * this.y) / 10d)); // 10% de las celdas tendrán peligros
        // Coloca al jugador en la celda de inicio (0,0) — constructando jugador con referencia al laberinto
        this.jugador = new Jugador("player@example.com", "password");
        this.jugador.celdaActual = maze[0][0];
        maze[0][0].addEntidad(this.jugador);
        for (int i = 0; i < nPeligro; i++) {
            int px, py;
            do {
                px = (int) (Math.random() * this.x);
                py = (int) (Math.random() * this.y);
            } while ((px == 0 && py == 0) || !maze[px][py].obtenerContenido().isEmpty());
            Trampa enemigo = (Math.round(Math.random())) == 0 ? new Trampa() : new Enemigo();
            enemigo.setPosition(px, py);
            maze[px][py].addEntidad(enemigo);
            entidades.add(enemigo);
        }
        boolean fin = false;
        // Start the player's input loop here so the program stays running using the generated maze.
        // The loop is implemented inside Jugador.method() and will exit when the player presses 'Q'.
        int eJugador = 0;
        this.display();
        while (!fin) {
            eJugador = this.jugador.movimiento(this);
            if (eJugador != 0) {
                fin = true;
                break;
            }
            for (Entidad e : entidades) {
                if (e instanceof Movimiento movimientoEntidad) {
                    movimientoEntidad.movimiento(this);
                }
            }
        }
    }

    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

    private static int clamp(int v) {
        return Math.max(MIN_DIM, Math.min(MAX_DIM, v));
    }

    public void display() {
        Misc.clearScreen();
        for (int i = 0; i < y; i++) {
            // crea la pared norte
            for (int j = 0; j < x; j++) {
                if ((maze[j][i].valor & DIR.N.bit) == 0) {
                    System.out.print("+---");
                } else {
                    System.out.print("+   ");
                }
            }
            System.out.println("+");
            // crea la pared oeste
            for (int j = 0; j < x; j++) {
                if ((maze[j][i].valor & DIR.W.bit) == 0) {
                    // closed west wall: print '|' then a space, the cell char and a trailing space => 4 chars
                    System.out.print("| " + maze[j][i].obtenerAscii() + " ");
                } else {
                    System.out.print("  " + maze[j][i].obtenerAscii() + " ");
                }
            }
            System.out.println("|");
        }
        // crea la pared sur
        for (int j = 0; j < x; j++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }

    public boolean movimientoEntidad(Entidad entidad, DIR direccion) {
        int jugadorX = entidad.getPosX();
        int jugadorY = entidad.getPosY();
        int destinoX = jugadorX + direccion.direccionX;
        int destinoY = jugadorY + direccion.direccionY;
        if (!between(destinoX, x) || !between(destinoY, y)) {
            return false; // out of bounds
        }

        if ((maze[jugadorX][jugadorY].valor & direccion.bit) == 0) {
            return false; // wall closed
        }

        maze[jugadorX][jugadorY].removeEntidad(entidad);
        maze[destinoX][destinoY].addEntidad(entidad);
        if (entidad instanceof Jugador jugador) {
            jugador.celdaActual = maze[destinoX][destinoY];
        }
        entidad.setPosition(destinoX, destinoY);
        return true;
    }

    private void generateMaze(int celdaX, int celdaY) {
        DIR[] direccion = DIR.values();
        Collections.shuffle(Arrays.asList(direccion));
        for (DIR dir : direccion) {
            int vecinoX = celdaX + dir.direccionX;
            int vecinoY = celdaY + dir.direccionY;
            if (between(vecinoX, x) && between(vecinoY, y)
                    && (maze[vecinoX][vecinoY].valor == 0)) {
                maze[celdaX][celdaY].valor |= dir.bit;
                maze[vecinoX][vecinoY].valor |= dir.opposite.bit;
                generateMaze(vecinoX, vecinoY);
            }
        }
    }

    public enum DIR {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);

        // utiliza el inicializador estático para resolver las referencias anticipadas
        static {
            N.opposite = S;
            S.opposite = N;
            E.opposite = W;
            W.opposite = E;
        }

        private final int bit;
        private final int direccionX;
        private final int direccionY;
        private DIR opposite;

        DIR(int bit, int direccionX, int direccionY) {
            this.bit = bit;
            this.direccionX = direccionX;
            this.direccionY = direccionY;
        }
    }

}
