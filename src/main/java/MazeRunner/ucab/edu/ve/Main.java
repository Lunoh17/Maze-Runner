package MazeRunner.ucab.edu.ve;

import java.util.Scanner;
import static MazeRunner.ucab.edu.ve.Menu.MenuInicio;

/**
 * Punto de entrada de la aplicación Maze Runner.
 * Orquesta el menú principal y el ciclo de juego/carga de partidas.
 */
public class Main {
    static Scanner scanner = new Scanner(System.in);

    /**
     * Punto de entrada. Inicia el flujo de autenticación y muestra el menú principal.
     * @param args argumentos de la línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        Laberinto lab;
        // Primero mostrar el menú de inicio de sesión/registro; solo continuar si la sesión inició
        Jugador jugadorSesion = MenuInicio();
        if (jugadorSesion == null) {
            System.out.println("Exiting application.");
            // Saliendo de la aplicación.
            return;
        }

        boolean salir = false;
        while (!salir) {
            System.out.println(Menu.mostrarMenu());
            switch (scanner.nextLine()) {
                case "1":
                    System.out.print("Introduzca el tamaño del laberinto (max 50): ");
                    int n = Integer.parseInt(scanner.nextLine());
                    lab = new Laberinto(n);
                    // inyectar el jugador autenticado en el nuevo laberinto
                    lab.setJugador(jugadorSesion);
                    lab.jugar();
                    break;
                case "2":
                    System.out.println("Cargar Juego");
                    lab = Laberinto.cargarJson(jugadorSesion.getCorreoElectronico());
                    if (lab != null) {
                        // si el juego cargado no tiene jugador (poco probable), usar el autenticado
                        if (lab.jugador == null) {
                            lab.setJugador(jugadorSesion);
                            // asegurar ubicación en (0,0) por si las posiciones no estaban en el JSON
                            lab.jugador.setPosition(0, 0);
                        }
                        lab.jugar();
                    } else {
                        System.out.println("No se pudo cargar el juego.");
                    }
                    break;
                case "3":
                    System.out.println("Estadistica");
                    Statistics.printAll();
                    break;
                case "4":
                    System.out.println("Salir");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción del 1 al 4.");

            }
        }
    }
}