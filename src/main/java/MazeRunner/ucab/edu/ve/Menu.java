package MazeRunner.ucab.edu.ve;

public class Menu {
    public static String pedirCorreo() {
        return "Por favor, ingrese su correo electrónico: ";
    }
    public static String pedirContrasenia() {
        return "Por favor, ingrese su contraseña: ";
    }
    public static Jugador pedirUsuario() {
        return null;
    }
    public static void menuIniciarSesión() {

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
