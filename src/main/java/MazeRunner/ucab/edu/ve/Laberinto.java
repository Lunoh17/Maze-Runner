package MazeRunner.ucab.edu.ve;

import java.util.Collections;
import java.util.Arrays;

public class Laberinto {
    private static final int MAX_DIM = 50;
    private static final int MIN_DIM = 1;
    private final int x;
    private final int y;
    private final Celda[][] maze;

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

        // Coloca al jugador en la celda de inicio (0,0)
        Jugador jugador = new Jugador("player@example.com", "password");
        jugador.celdaActual = maze[0][0];
        maze[0][0].addEntidad(jugador);
    }

    public void display() {
        for (int i = 0; i < y; i++) {
            // crea la pared norte
            for (int j = 0; j < x; j++) {
                if ((maze[j][i].valor & DIR.N.bit) == 0) {
                    System.out.print("+---");
                }
                else {
                    System.out.print("+   ");
                }
            }
            System.out.println("+");
            // crea la pared oeste
            for (int j = 0; j < x; j++) {
                if ((maze[j][i].valor & DIR.W.bit) == 0) {
                    // closed west wall: print '|' then a space, the cell char and a trailing space => 4 chars
                    System.out.print("| " + maze[j][i].obtenerAscii() + " ");
                }
                else {
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

    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

    private static int clamp(int v) {
        return Math.max(MIN_DIM, Math.min(MAX_DIM, v));
    }

    private enum DIR {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
        private final int bit;
        private final int direccionX;
        private final int direccionY;
        private DIR opposite;

        // utiliza el inicializador estático para resolver las referencias anticipadas
        static {
            N.opposite = S;
            S.opposite = N;
            E.opposite = W;
            W.opposite = E;
        }

        DIR(int bit, int direccionX, int direccionY) {
            this.bit = bit;
            this.direccionX = direccionX;
            this.direccionY = direccionY;
        }
    };
}
