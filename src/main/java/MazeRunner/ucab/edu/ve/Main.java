package MazeRunner.ucab.edu.ve;
import java.util.Scanner;

public class Main {
    static Scanner scanner=new Scanner(System.in);
    Jugador jugador=new Jugador("Correo Electronico:","Contraseña: ");
    public static void main(String[] args) {

        System.out.println(Menu.mostrarMenu());
        switch (scanner.nextLine()) {
            case "1":
                System.out.print("Introduzca el tamaño del laberinto (max 50): ");
                int n = Integer.parseInt(scanner.nextLine());
                Laberinto lab = new Laberinto(n);
                lab.display();
                break;
            case "2":
                System.out.println("Cargar Juego");
                break;
            case "3":
                System.out.println("Estadistica");
                break;
            case "4":
                System.out.println("Salir");
                break;
            default:
                System.out.println("Opción no válida. Por favor, seleccione una opción del 1 al 4.");

        }
    }
}