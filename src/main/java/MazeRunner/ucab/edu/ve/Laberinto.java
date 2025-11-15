package MazeRunner.ucab.edu.ve;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Laberinto {
    private static final int MAX_DIM = 50;
    private static final int MIN_DIM = 1;
    private final int x;
    private final int y;
    private final Celda[][] maze;
    // store the player so it persists and can be used to start the input loop
    public Jugador jugador;
    private final Vector<Entidad> entidades = new Vector<>();

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
    }

    public void jugar(){
        boolean fin = false;
        // Start the player's input loop here so the program stays running usando el laberinto generado.
        // The loop is implementado inside Jugador.method() and will exit when the player presses 'Q'.
        int eJugador = 0;
        this.display();
        while (!fin) {
            eJugador = this.jugador.movimiento(this);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            if (eJugador != 0) {
                ControladorBD.guardar(this);
                fin = true;
                break;
            }
            for (Entidad e : entidades) {
                if (e instanceof Movimiento movimientoEntidad) {
                    movimientoEntidad.movimiento(this);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            if (jugador.celdaActual.cantidadEntidades() > 1) {
                for (Entidad e : jugador.celdaActual.obtenerContenido()) {
                    e.interact(jugador);
                }
            }
        }
    }

    // Private constructor used by cargarJson to build the empty grid without starting the loop
    private Laberinto(int x, int y, boolean skipGameLoop) {
        this.x = x;
        this.y = y;
        maze = new Celda[this.x][this.y];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = new Celda();
            }
        }
    }

    /**
     * Carga el laberinto desde el archivo laberinto.json ubicado en el directorio del proyecto.
     * Reconstruye celdas, jugador y entidades, y enlaza referencias.
     */
    public static Laberinto cargarJson() {
        String projectRoot = System.getProperty("user.dir");
        File inFile = new File(projectRoot, "laberinto.json");
        if (!inFile.exists()) {
            System.err.println("No se encontró laberinto.json en: " + inFile.getAbsolutePath());
            return null;
        }
        try (FileReader fr = new FileReader(inFile)) {
            JsonObject root = JsonParser.parseReader(fr).getAsJsonObject();
            int x = root.has("x") ? root.get("x").getAsInt() : 0;
            int y = root.has("y") ? root.get("y").getAsInt() : 0;
            Laberinto lab = new Laberinto(x, y, true);

            // Fill maze cell values and contents
            if (root.has("maze")) {
                JsonArray mazeArray = root.getAsJsonArray("maze");
                for (int i = 0; i < mazeArray.size() && i < lab.maze.length; i++) {
                    JsonArray col = mazeArray.get(i).getAsJsonArray();
                    for (int j = 0; j < col.size() && j < lab.maze[i].length; j++) {
                        JsonObject cellObj = col.get(j).getAsJsonObject();
                        if (cellObj.has("valor")) {
                            lab.maze[i][j].valor = cellObj.get("valor").getAsInt();
                        }
                        // contents will be reconstructed below using root.entidades and root.jugador primarily
                    }
                }
            }

            // First reconstruct jugador if present at root
            if (root.has("jugador")) {
                JsonObject jObj = root.getAsJsonObject("jugador");
                Jugador j = Jugador.fromJson(jObj);
                lab.jugador = j;
                // place jugador in the maze if valid positions exist
                if (j.getPosX() >= 0 && j.getPosY() >= 0 && j.getPosX() < lab.x && j.getPosY() < lab.y) {
                    j.celdaActual = lab.maze[j.getPosX()][j.getPosY()];
                    lab.maze[j.getPosX()][j.getPosY()].addEntidad(j);
                }
            }

            // Reconstruct entidades list from root.entidades (preferred) or from scanning cells
            if (root.has("entidades")) {
                JsonArray ents = root.getAsJsonArray("entidades");
                for (JsonElement ee : ents) {
                    JsonObject eo = ee.getAsJsonObject();
                    Entidad entidad = crearEntidadDesdeJson(eo);
                    if (entidad != null) {
                        int px = entidad.getPosX();
                        int py = entidad.getPosY();
                        if (px >= 0 && py >= 0 && px < lab.x && py < lab.y) {
                            lab.maze[px][py].addEntidad(entidad);
                        }
                        if (entidad instanceof Trampa || entidad instanceof Enemigo) {
                            lab.entidades.add(entidad);
                        }
                    }
                }
            } else {
                // fallback: scan cells for contenido arrays
                if (root.has("maze")) {
                    JsonArray mazeArray = root.getAsJsonArray("maze");
                    for (int i = 0; i < mazeArray.size() && i < lab.maze.length; i++) {
                        JsonArray col = mazeArray.get(i).getAsJsonArray();
                        for (int j = 0; j < col.size() && j < lab.maze[i].length; j++) {
                            JsonObject cellObj = col.get(j).getAsJsonObject();
                            if (cellObj.has("contenido")) {
                                JsonArray content = cellObj.getAsJsonArray("contenido");
                                for (JsonElement ce : content) {
                                    Entidad entidad = crearEntidadDesdeJson(ce.getAsJsonObject());
                                    if (entidad != null) {
                                        lab.maze[i][j].addEntidad(entidad);
                                        if (entidad instanceof Trampa || entidad instanceof Enemigo) {
                                            lab.entidades.add(entidad);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return lab;
        } catch (IOException ex) {
            System.err.println("Error leyendo laberinto.json: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    // Helper to construct appropriate Entidad subclass from JSON representation
    private static Entidad crearEntidadDesdeJson(JsonObject eo) {
        if (eo == null) return null;
        // Player is handled separately
        if (eo.has("correoElectronico") || eo.has("contrasenia")) {
            return Jugador.fromJson(eo);
        }
        char ascii = eo.has("ascii") ? eo.get("ascii").getAsString().charAt(0) : '?';
        if (ascii == 'E') {
            Enemigo en = new Enemigo();
            if (eo.has("danio")) {
                try {
                    java.lang.reflect.Field f = Trampa.class.getDeclaredField("danio");
                    f.setAccessible(true);
                    f.setShort(en, (short) eo.get("danio").getAsInt());
                } catch (Exception ignored) {
                }
            }
            if (eo.has("posX") && eo.has("posY")) {
                en.setPosition(eo.get("posX").getAsInt(), eo.get("posY").getAsInt());
            }
            return en;
        } else if (ascii == 'T') {
            Trampa t = new Trampa();
            if (eo.has("danio")) {
                try {
                    java.lang.reflect.Field f = Trampa.class.getDeclaredField("danio");
                    f.setAccessible(true);
                    f.setShort(t, (short) eo.get("danio").getAsInt());
                } catch (Exception ignored) {
                }
            }
            if (eo.has("posX") && eo.has("posY")) {
                t.setPosition(eo.get("posX").getAsInt(), eo.get("posY").getAsInt());
            }
            return t;
        } else if (ascii == 'K') {
            Llave k = new Llave();
            if (eo.has("posX") && eo.has("posY")) {
                k.setPosition(eo.get("posX").getAsInt(), eo.get("posY").getAsInt());
            }
            return k;
        } else {
            // unknown entity: create a generic anonymous Entidad to hold position and ascii
            Entidad e = new Entidad() {
                @Override
                public void interact(Jugador player) {
                }
            };
            e.ascii = ascii;
            if (eo.has("posX") && eo.has("posY")) {
                e.setPosition(eo.get("posX").getAsInt(), eo.get("posY").getAsInt());
            }
            return e;
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
