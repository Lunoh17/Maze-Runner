package MazeRunner.ucab.edu.ve;
import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    public static String pedirCorreo() {
        System.out.print("Por favor, ingrese su correo electrónico: ");
        return scanner.nextLine().trim();
    }

    public static String pedirContrasenia() {
        System.out.print("Por favor, ingrese su contraseña: ");
        return scanner.nextLine();
    }

    public static Jugador menuIniciarSesion() {
        String correo = pedirCorreo();
        String contrasenia = pedirContrasenia();
        return new Jugador(correo, contrasenia);
    }

    public static String mostrarMenu() {
        String menu = "----- MENÚ PRINCIPAL -----\n"
                + "1. Iniciar Juego\n"
                + "2. Cargar Juego\n"
                + "3. Estadistica\n"
                + "4. Salir\n"
                + "Seleccione una opción: ";
        return menu;
    }
}
