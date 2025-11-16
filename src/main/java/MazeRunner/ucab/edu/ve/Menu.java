package MazeRunner.ucab.edu.ve;

import java.util.Scanner;

/**
 * Proporciona las interfaces de consola para registro, inicio de sesión y navegación del menú.
 */
public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Solicita un correo electrónico al usuario.
     * @return texto ingresado como correo
     */
    public static String pedirCorreo() {
        System.out.print("Por favor, ingrese su correo electrónico: ");
        return scanner.nextLine().trim();
    }

    /**
     * Solicita una contraseña al usuario.
     * @return texto ingresado como contraseña
     */
    public static String pedirContrasenia() {
        System.out.print("Por favor, ingrese su contraseña: ");
        return scanner.nextLine();
    }

    /**
     * Crea un objeto Jugador a partir de las credenciales solicitadas por consola.
     * No valida contra archivo; solo construye la instancia.
     * @return nuevo Jugador con correo y contraseña ingresados
     */
    public static Jugador menuIniciarSesion() {
        String correo = pedirCorreo();
        String contrasenia = pedirContrasenia();
        return new Jugador(correo, contrasenia);
    }

    /**
     * Genera el texto del menú principal.
     * @return representación de menú para imprimir
     */
    public static String mostrarMenu() {
        String menu = "----- MENÚ PRINCIPAL -----\n"
                + "1. Iniciar Juego\n"
                + "2. Cargar Juego\n"
                + "3. Estadistica\n"
                + "4. Salir\n"
                + "Seleccione una opción: ";
        return menu;
    }

    // Devuelve el Jugador autenticado si inicia sesión correctamente, o null si decide salir
    /**
     * Flujo principal de autenticación/registro/recuperación desde consola.
     * @return Jugador autenticado si inicia sesión; null si el usuario decide salir
     */
    public static Jugador MenuInicio(){
        int opciones;
        System.out.println("Bienvenido al Juego Maze Runner");
        System.out.println("-------------------------------");
        do{
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar Sesion");
            System.out.println("3. Recuperar Contraseña");
            System.out.println("0. Salir");
            String line = scanner.nextLine();
            if(line == null || line.isEmpty()) {
                System.out.println("Opcion Erronea");
                continue;
            }
            try {
                opciones = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Opcion Erronea");
                continue;
            }
            switch (opciones){
                case 1:
                    MenuRegistro();
                    break;
                case 2: {
                    Jugador j = MenuSesion();
                    if (j != null) {
                        return j;
                        // sesión iniciada, continuar al menú principal con este Jugador
                    }
                    break;
                }
                case 3:
                    MenuRecuperar();
                    break;
                case 0:
                    return null;
                    // el usuario eligió salir
                default:
                    System.out.println("Opcion Erronea");
            }
        }while(true);
    }

    /**
     * Flujo de registro de un nuevo usuario solicitando correo y contraseña.
     */
    public static void MenuRegistro(){
        Usuario AuxUsuario = new Usuario();
        Contrasenia AuxContrasenia = new Contrasenia();
        System.out.println("-----------------------Registro----------------------");
        System.out.println("-----------------------------------------------------");
        do{
            System.out.println("-Ingrese Correo (ejemplo@ // ejemPlo@dominio.com)");
            System.out.println("-----------------------------------------------------");
            Usuario usuario = new Usuario(scanner.next());
            if(usuario.getCorreo() != null){
                AuxUsuario = usuario;
            }else{
                AuxUsuario = null;
            }
            scanner.nextLine();
            // consumir el resto de la línea si es necesario
        }while(AuxUsuario == null);
        do{
            System.out.println("-----------------------------------------------------");
            System.out.println("-Ingrese Contraseña y Su Confirmacion de Contraseña ");
            Contrasenia contrasenia = new Contrasenia(scanner.next(),scanner.next());
            if(contrasenia.getContrasenia() != null){
                AuxContrasenia = contrasenia;
                System.out.println("-----------------------------------------------------");
            }else{
                AuxContrasenia = null;
            }
            scanner.nextLine();
            // consumir el resto de la línea
        }while(AuxContrasenia == null);
        CompararDatos comparar = new CompararDatos(AuxUsuario,AuxContrasenia);
        if((!comparar.EnviarDatosRegistro())){
            GuardarDatos guardarDatos =  new GuardarDatos(AuxUsuario,AuxContrasenia);
            guardarDatos.FormatoRegistro();
            guardarDatos.GuardarFormatoRegistro();
        }
    }

    // devuelve el Jugador autenticado si el inicio de sesión tiene éxito, de lo contrario null
    /**
     * Flujo para solicitar credenciales e intentar autenticación contra el archivo de registros.
     * @return Jugador autenticado si las credenciales son correctas; null en caso contrario
     */
    public static Jugador MenuSesion(){
        Usuario AuxUsuario = new Usuario();
        Contrasenia AuxContrasenia = new Contrasenia();
        System.out.println("-------------------Iniciar Sesion--------------------");
        System.out.println("-----------------------------------------------------");
        do{
            System.out.println("-Ingrese Usuario: ");
            Usuario usuario = new Usuario(scanner.next());
            if(usuario.getCorreo() != null){
                AuxUsuario = usuario;
            }else{
                AuxUsuario = null;
            }
            scanner.nextLine();
            // consumir el resto
        }while(AuxUsuario == null);
        do{
            System.out.println("-----------------------------------------------------");
            System.out.println("-Ingrese Contraseña ");
            Contrasenia contrasenia = new Contrasenia(scanner.next());
            if(contrasenia.getContrasenia() != null){
                AuxContrasenia = contrasenia;
            }else{
                AuxContrasenia = null;
            }
            scanner.nextLine();
            // consumir el resto
        }while(AuxContrasenia == null);
        CompararDatos comparar = new CompararDatos(AuxUsuario,AuxContrasenia);
        if(comparar.EnviarDatosSesion()){
            System.out.println("-----------------------------------------------------");
            System.out.println("--------------Inicio de Sesion Exitosa---------------");
            System.out.println("-----------------------------------------------------");
            System.out.println("Bienvenido "+ AuxUsuario.getCorreo());
            // crear y devolver el Jugador que representa a este usuario autenticado
            return new Jugador(AuxUsuario.getCorreo(), AuxContrasenia.getContrasenia());
        }else{
            System.out.println("-----------------------------------------------------");
            System.out.println("❌ERROR Usuario No Registrado/Contraseña Incorrecta");
            System.out.println("-----------------------------------------------------");
            return null;
        }
    }

    /**
     * Flujo de recuperación de contraseña mostrando la información si existe el usuario.
     */
    public static void MenuRecuperar() {
        Usuario AuxUsuario = new Usuario();
        System.out.println("----------------Recuperar Contraseña-----------------");
        System.out.println("-----------------------------------------------------");
        do {
            System.out.println("-Ingrese Usuario: ");
            Usuario usuario = new Usuario(scanner.next());
            if (usuario.getCorreo() != null) {
                AuxUsuario = usuario;
            } else {
                AuxUsuario = null;
            }
            scanner.nextLine();
            // consumir el resto
        } while (AuxUsuario == null);
        CompararDatos comparar = new CompararDatos(AuxUsuario);
        if (comparar.EnviarDatosContrasenia()) {
            System.out.println("-----------------------------------------------------");
            System.out.println("Recuperacion Exitosa");
        } else {
            System.out.println("-----------------------------------------------------");
            System.out.println("Usuario No Registrado");
        }
    }
}
